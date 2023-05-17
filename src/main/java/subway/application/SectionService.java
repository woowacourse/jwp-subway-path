package subway.application;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
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
        validateExists(sectionRequest);
        final Sections sections = findSections(sectionRequest.getLineId());

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

    private void validateExists(final SectionRequest sectionRequest) {
        if (lineDao.notExistsById(sectionRequest.getLineId())) {
            throw new NoSuchElementException("해당하는 호선이 존재하지 않습니다.");
        }
        if (stationDao.notExistsById(sectionRequest.getUpStationId())
                || stationDao.notExistsById(sectionRequest.getDownStationId())) {
            throw new NoSuchElementException("해당하는 역이 존재하지 않습니다.");
        }
    }

    public void delete(final Long lineId, final Long stationId) {
        final Sections sections = findSections(lineId);
        final Station station = stationDao.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                .toDomain();
        sections.removeSectionByStation(station);

        sectionDao.deleteAllByLineId(lineId);
        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(it -> SectionEntity.toEntity(lineId, it))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
    }

    private Sections findSections(final Long lineId) {
        return new Sections(
                sectionDao.findByLineId(lineId).stream()
                        .map(it -> it.toDomain(
                                stationDao.findById(it.getUpStationId()).orElseThrow().toDomain(),
                                stationDao.findById(it.getDownStationId()).orElseThrow().toDomain()))
                        .collect(Collectors.toList())
        );
    }

    public List<SectionResponse> findByLineId(final Long lineId) {
        return sectionDao.findByLineId(lineId).stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }
}
