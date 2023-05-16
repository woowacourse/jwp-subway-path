package subway.application;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
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

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void save(final SectionRequest sectionRequest) {
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
}
