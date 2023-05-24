package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.exception.NotExistException;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.LineEntity;
import subway.persistence.dao.entity.SectionEntity;
import subway.persistence.dao.entity.StationEntity;
import subway.service.line.domain.Line;
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
public class H2StationsInLineRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public H2StationsInLineRepository(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
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

        Map<Long, StationEntity> stationEntities = stationDao.findStationsById(uniqueStationIds).stream()
                .collect(Collectors.toMap(StationEntity::getId, stationEntity -> stationEntity));
        List<Section> sections = makeSections(sectionEntities, stationEntities);

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

        return makeSectionsPerLine(sectionEntitiesPerLineId);
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

    @Override
    public List<Section> findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Set<Long> uniqueStationIds = makeUniqueStationIds(sectionEntities);

        Map<Long, StationEntity> stationEntities = stationDao.findStationsById(uniqueStationIds).stream()
                .collect(Collectors.toMap(StationEntity::getId, stationEntity -> stationEntity));

        return makeSections(sectionEntities, stationEntities);
    }

    @Override
    public Map<Line, Sections> findAllSectionsPerLine() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        if (sectionEntities.isEmpty()) {
            return new HashMap<>();
        }

        Map<Long, List<SectionEntity>> sectionEntitiesPerLineId = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLindId));

        return makeSectionsPerLine(sectionEntitiesPerLineId);
    }


    private Map<Line, Sections> makeSectionsPerLine(Map<Long, List<SectionEntity>> sectionEntitiesPerLineId) {
        Map<Line, Sections> sectionsPerLine = new HashMap<>();
        for (Long lineId : sectionEntitiesPerLineId.keySet()) {
            LineEntity lineEntity = lineDao.findLineById(lineId).orElseThrow(
                    () -> new NotExistException("존재하지 않는 노선입니다.")
            );
            Line line = Line.from(lineEntity);
            List<SectionEntity> entities = sectionEntitiesPerLineId.get(lineId);
            Set<Long> uniqueStationIds = makeUniqueStationIds(entities);

            Map<Long, StationEntity> stationEntities = stationDao.findStationsById(uniqueStationIds).stream()
                    .collect(Collectors.toMap(StationEntity::getId, stationEntity -> stationEntity));
            Sections sections = new Sections(makeSections(entities, stationEntities));
            sectionsPerLine.put(line, sections);
        }
        return sectionsPerLine;
    }

    private List<Section> makeSections(List<SectionEntity> sectionEntities, Map<Long, StationEntity> stationEntityMap) {
        ArrayList<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            Section section = Section.of(sectionEntity, stationEntityMap);
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
}
