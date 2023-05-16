package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public LineRepository(final StationDao stationDao, final SectionDao sectionDao, final LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public List<Line> findAll() {
        List<LineEntity> allLines = lineDao.findAll();
        Map<Long, Station> allStationsById = stationDao.findAll().stream()
                .map(StationEntity::convertToStation)
                .collect(Collectors.toMap(Station::getId, station -> station));
        Map<Long, List<Section>> allSectionsByLindId = getAllSectionsByLineId(allStationsById);
        return allLines.stream()
                .map(lineEntity -> lineEntity.convertToLine(allSectionsByLindId.get(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 라인이 없습니다."));
        Map<Long, Station> allStationsById = stationDao.findAll().stream()
                .map(StationEntity::convertToStation)
                .collect(Collectors.toMap(Station::getId, station -> station));
        Map<Long, List<Section>> allSectionsByLindId = getAllSectionsByLineId(allStationsById);
        return lineEntity.convertToLine(allSectionsByLindId.get(lineEntity.getId()));
    }

    private Map<Long, List<Section>> getAllSectionsByLineId(final Map<Long, Station> allStationsById) {
        Map<Long, List<Section>> allSectionsByLindId = new HashMap<>();
        List<SectionEntity> allSections = sectionDao.findAll();
        for (SectionEntity sectionsEntity : allSections) {
            Long lineId = sectionsEntity.getLineId();
            List<Section> sections = allSectionsByLindId.getOrDefault(lineId, new ArrayList<>());
            sections.add(sectionsEntity.convertToSection(allStationsById));
            allSectionsByLindId.put(lineId, sections);
        }
        return allSectionsByLindId;
    }

    public void update(final Line before, final Line update) {
        List<Section> beforeSections = before.getSections();
        beforeSections.removeAll(update.getSections());
        List<SectionEntity> beforeSectionEntities = convertToSectionEntities(before, beforeSections);
        sectionDao.delete(beforeSectionEntities);

        List<Station> beforeStation = before.findAllStation();
        beforeStation.removeAll(update.findAllStation());
        List<StationEntity> beforeStationEntities = convertToStationEntities(beforeStation);
        stationDao.delete(beforeStationEntities);

        List<Station> updateStations = update.findAllStation();
        updateStations.removeAll(before.findAllStation());
        List<StationEntity> updateStationEntities = convertToStationEntities(updateStations);
        stationDao.save(updateStationEntities);

        List<Section> updateSections = update.getSections();
        updateSections.removeAll(before.getSections());
        List<SectionEntity> updateSectionEntities = convertToSectionEntities(update, updateSections);
        sectionDao.save(updateSectionEntities);
    }

    private List<StationEntity> convertToStationEntities(final List<Station> stations) {
        return stations.stream()
                .map(StationEntity::from)
                .collect(Collectors.toList());
    }

    private List<SectionEntity> convertToSectionEntities(final Line line, final List<Section> sections) {
        return sections.stream()
                .map(section -> SectionEntity.of(section, line))
                .collect(Collectors.toList());
    }
}
