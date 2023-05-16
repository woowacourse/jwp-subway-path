package subway.application;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void save(final SectionRequest sectionRequest) {
        final Sections sections = new Sections(
                sectionDao.findByLineId(sectionRequest.getLineId()).stream()
                .map(it -> it.toDomain(
                        stationDao.findById(it.getUpStationId()).orElseThrow().toDomain(),
                        stationDao.findById(it.getDownStationId()).orElseThrow().toDomain()))
                .collect(Collectors.toList())
        );

        final Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                .toDomain();
        final Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                .toDomain();
        final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        sections.addSection(section);

        sectionDao.deleteAllByLineId(sectionRequest.getLineId());

        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(it -> SectionEntity.toEntity(sectionRequest.getLineId(), it))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
    }

    // TODO
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.isEmpty()) {
            throw new NoSuchElementException("해당하는 값이 없습니다.");
        }

        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
        // TODO: 라인에 역이 2개만 있는 경우, 삭제 로직 수정 필요
        if (sectionEntities.size() == 1) {
            linkIfFinalStation(lineId, stationId, sectionEntities.get(0));
            return;
        }
        link(lineId, stationId, sectionEntities);
    }

    private void linkIfFinalStation(final Long lineId, final Long stationId, final SectionEntity section) {
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return;
        }
        if (section.getDownStationId().equals(stationId)) {
            Long upStationId = section.getUpStationId();
            sectionDao.insert(new SectionEntity(lineId, upStationId, null, 0));
            return;
        }
        Long downStationId = section.getDownStationId();
        sectionDao.insert(new SectionEntity(lineId, null, downStationId, 0));
    }

    private void link(final Long lineId, final Long stationId, final List<SectionEntity> sectionEntities) {
        SectionEntity previousSection = sectionEntities.get(0);
        SectionEntity nextSection = sectionEntities.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStationId().equals(stationId)) {
            Long upStationId = nextSection.getUpStationId();
            Long downStationId = previousSection.getDownStationId();
            sectionDao.insert(new SectionEntity(lineId, upStationId, downStationId, newDistance));
            return;
        }
        Long upStationId = previousSection.getUpStationId();
        Long downStationId = nextSection.getDownStationId();
        sectionDao.insert(new SectionEntity(lineId, upStationId, downStationId, newDistance));
    }
}
