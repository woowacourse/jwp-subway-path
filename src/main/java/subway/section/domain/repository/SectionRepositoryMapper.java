package subway.section.domain.repository;

import org.springframework.stereotype.Repository;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.entity.SectionEntity;
import subway.section.exception.SectionNotFoundException;
import subway.station.domain.Station;
import subway.station.domain.repository.StationDao;
import subway.station.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionRepositoryMapper implements SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepositoryMapper(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }


    @Override
    public Long insert(final Section section) {
        return sectionDao.insert(section.toEntity());
    }

    @Override
    public Section findById(final Long id) {
        return sectionDao.findById(id)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Sections findByLineId(final Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        return new Sections(getSections(sectionEntities));
    }

    @Override
    public Section findByUpStationId(final Long upStationId) {
        return sectionDao.findByUpStationId(upStationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Optional<SectionEntity> findOptionalByUpStationId(final Long upStationId) {
        return sectionDao.findByUpStationId(upStationId);
    }

    @Override
    public Section findLeftSectionByStationId(final Long stationId) {
        return sectionDao.findLeftSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Section findRightSectionByStationId(final Long stationId) {
        return sectionDao.findRightSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public List<Section> findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        return getSections(sectionEntities);
    }

    private List<Section> getSections(final List<SectionEntity> sectionEntities) {
        List<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            Station upStation = findStationById(sectionEntity.getUpStationId());
            Station downStation = findStationById(sectionEntity.getDownStationId());
            sections.add(Section.of(sectionEntity.getId(), upStation, downStation, sectionEntity.getDistance()));
        }
        return sections;
    }

    private Station findStationById(final Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public void deleteById(final Long id) {
        sectionDao.deleteById(id);
    }

}
