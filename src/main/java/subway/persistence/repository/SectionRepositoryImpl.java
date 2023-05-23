package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.*;
import subway.domain.repository.SectionRepository;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionRepositoryImpl implements SectionRepository {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepositoryImpl(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public void saveAll(Long lineId, Sections sections) {
        List<SectionEntity> sectionEntities = toSectionEntities(lineId, sections.getSections());

        sectionDao.insertAll(sectionEntities);
    }

    private List<SectionEntity> toSectionEntities(Long lineId, List<Section> sections) {
        Map<String, Long> stationsIdByName = stationDao.findAll().stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        return sections.stream()
                .map(it -> {
                    Station startStation = it.getStartStation();
                    Station endStation = it.getEndStation();

                    Long startStationId = stationsIdByName.get(startStation.getName());
                    Long endStationId = stationsIdByName.get(endStation.getName());

                    return new SectionEntity(lineId, startStationId, endStationId, it.getDistance());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> findAll() {
        return sectionDao.findAll().stream()
                .map(this::toSection)
                .collect(Collectors.toList());
    }

    private Section toSection(SectionEntity sectionEntity) {
        StationEntity startStationEntity = stationDao.findById(sectionEntity.getStartStationId());
        StationEntity endStationEntity = stationDao.findById(sectionEntity.getEndStationId());

        Station startStation = toStation(startStationEntity);
        Station endStation = toStation(endStationEntity);
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

    @Override
    public Sections findAllByLineId(Long lineId) {
        List<Section> sections = sectionDao.findByLineId(lineId)
                .stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }

    @Override
    public void deleteAllByLineId(Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
    }

    @Override
    public boolean isExistSectionUsingStation(Long stationId) {
        return sectionDao.isExistSectionUsingStation(stationId);
    }
}
