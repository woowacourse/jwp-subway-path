package subway.service.section.repository;

import org.springframework.stereotype.Repository;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.persistence.dao.entity.StationEntity;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.station.domain.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Section insertSection(Section section, Line line) {
        SectionEntity sectionEntity = new SectionEntity(
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance().getDistance(),
                line.getId()
        );
        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);
        return new Section(
                savedSectionEntity.getId(),
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance()
        );
    }

    public Sections findSectionsByLine(Line line) {
        List<SectionEntity> sectionEntities = sectionDao.findSectionsByLine(line.getId());
        if (sectionEntities.isEmpty()) {
            return new Sections(List.of());
        }
        Set<Long> uniqueStationIds = makeUniqueStationIds(sectionEntities);

        Map<Long, StationEntity> stationEntityMap = stationDao.findStationsById(uniqueStationIds).stream()
                .collect(Collectors.toMap(StationEntity::getStationId, stationEntity -> stationEntity));
        ArrayList<Section> sections = makeSections(sectionEntities, stationEntityMap);

        return new Sections(sections);
    }

    private static ArrayList<Section> makeSections(List<SectionEntity> sectionEntities, Map<Long, StationEntity> stationEntityMap) {
        ArrayList<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            StationEntity upStationEntity = stationEntityMap.get(sectionEntity.getUpStationId());
            Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());

            StationEntity downStationEntity = stationEntityMap.get(sectionEntity.getDownStationId());
            Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());
            sections.add(new Section(
                    sectionEntity.getId(),
                    upStation,
                    downStation,
                    new Distance(sectionEntity.getDistance())
            ));
        }
        return sections;
    }

    private static Set<Long> makeUniqueStationIds(List<SectionEntity> sectionEntities) {
        List<Long> upStationIds = sectionEntities.stream()
                .map(SectionEntity::getUpStationId)
                .collect(Collectors.toList());

        List<Long> downStationIds = sectionEntities.stream()
                .map(SectionEntity::getDownStationId)
                .collect(Collectors.toList());

        upStationIds.addAll(downStationIds);
        return new HashSet<>(upStationIds);
    }
}
