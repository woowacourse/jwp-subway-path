package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.persistence.dao.entity.StationEntity;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.repository.SectionRepository;
import subway.service.station.domain.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionRepositoryImpl(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Override
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

    @Override
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

    @Override
    public Map<Line, Sections> findSectionsByStation(Station station) {
        List<SectionEntity> sectionEntities = sectionDao.findSectionsByStation(station.getId());
        if (sectionEntities.isEmpty()) {
            return new HashMap<>();
        }

        Map<Long, List<SectionEntity>> sectionEntitiesPerLineId = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLindId));

        Map<Line, Sections> sectionsPerLine = new HashMap<>();
        for (Long lineId : sectionEntitiesPerLineId.keySet()) {
            Line line = lineDao.findById(lineId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
            List<SectionEntity> entities = sectionEntitiesPerLineId.get(lineId);
            Set<Long> uniqueStationIds = makeUniqueStationIds(entities);

            Map<Long, StationEntity> stationEntityMap = stationDao.findStationsById(uniqueStationIds).stream()
                    .collect(Collectors.toMap(StationEntity::getStationId, stationEntity -> stationEntity));
            Sections sections = new Sections(makeSections(entities, stationEntityMap));
            sectionsPerLine.put(line, sections);
        }

        return sectionsPerLine;
    }

    @Override
    public void deleteSection(Section section) {
        sectionDao.delete(section.getId());
    }

    @Override
    public boolean isLastSectionInLine(Line line) {
        List<SectionEntity> sectionsByLine = sectionDao.findSectionsByLine(line.getId());
        return sectionsByLine.size() == 1;
    }

    private ArrayList<Section> makeSections(List<SectionEntity> sectionEntities, Map<Long, StationEntity> stationEntityMap) {
        ArrayList<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            Section section = toDomain(sectionEntity, stationEntityMap);
            sections.add(section);
        }
        return sections;
    }

    private Set<Long> makeUniqueStationIds(List<SectionEntity> sectionEntities) {
        List<Long> upStationIds = sectionEntities.stream()
                .map(SectionEntity::getUpStationId)
                .collect(Collectors.toList());

        List<Long> downStationIds = sectionEntities.stream()
                .map(SectionEntity::getDownStationId)
                .collect(Collectors.toList());

        upStationIds.addAll(downStationIds);
        return new HashSet<>(upStationIds);
    }

    private SectionEntity toEntity(Section section, Line line) {
        return new SectionEntity(
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance().getDistance(),
                line.getId()
        );
    }

    private Section toDomain(SectionEntity sectionEntity, Map<Long, StationEntity> stationEntityMap) {

        StationEntity upStationEntity = stationEntityMap.get(sectionEntity.getUpStationId());
        Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());

        StationEntity downStationEntity = stationEntityMap.get(sectionEntity.getDownStationId());
        Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());
        return new Section(
                sectionEntity.getId(),
                upStation,
                downStation,
                new Distance(sectionEntity.getDistance())
        );
    }
}
