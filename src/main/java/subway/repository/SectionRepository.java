package subway.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class SectionRepository {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionRepository(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public void batchSave(Line line) {
        List<Section> sections = line.getSectionsByList();
        List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> new SectionEntity(
                        section.getUpStation().getId(),
                        section.getDownStation().getId(),
                        line.getId(),
                        section.getDistance()))
                .collect(Collectors.toList());
        sectionDao.batchSave(sectionEntities);
    }

    public void deleteByLineId(Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }

    public List<Section> findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        return convertToSection(sectionEntities);
    }

    public List<Section> findByLineId(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        return convertToSection(sectionEntities);
    }

    private List<Section> convertToSection(List<SectionEntity> sectionEntities) {
        Map<Long, String> stationEntities = findStations();
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        new Station(sectionEntity.getUpStationId(),
                                stationEntities.get(sectionEntity.getUpStationId())),
                        new Station(sectionEntity.getDownStationId(),
                                stationEntities.get(sectionEntity.getDownStationId())),
                        sectionEntity.getDistance()
                )).collect(Collectors.toList());
    }

    private Map<Long, String> findStations() {
        return stationDao.findAll().stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        StationEntity::getName)
                );
    }
}
