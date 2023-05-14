package subway.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineRepository(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public Long save(Line line) {
        Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isEmpty()) {
            LineEntity savedLineEntity = lineDao.insert(new LineEntity(line.getName()));
            updateSections(line, savedLineEntity.getId());
            return savedLineEntity.getId();
        }
        LineEntity savedLineEntity = lineEntity.get();
        updateLine(line, savedLineEntity.getId());
        return savedLineEntity.getId();
    }

    private void updateLine(Line line, Long lineId) {
        List<StationEntity> savedStations = stationDao.findAll();

        updateSections(line, lineId);

        List<StationEntity> afterDeleteStations = stationDao.findAllBySections();
        savedStations.removeAll(afterDeleteStations);

        List<Long> collect = savedStations.stream().map(StationEntity::getId).collect(toList());
        stationDao.deleteByIds(collect);
    }


    private void updateSections(Line line, Long lineId) {
        Map<String, StationEntity> nameByStations = line.getStations().stream()
                .map(station -> findOrSaveStation(station.getName()))
                .collect(toMap(StationEntity::getName, it -> it));
        sectionDao.deleteByLineId(lineId);
        for (Section section : line.getSections()) {
            Station source = section.getSource();
            Station target = section.getTarget();
            StationEntity sourceStationEntity = nameByStations.get(source.getName());
            StationEntity targetStationEntity = nameByStations.get(target.getName());
            sectionDao.insert(new SectionEntity(
                    sourceStationEntity.getId(),
                    targetStationEntity.getId(),
                    lineId,
                    section.getDistance()
            ));
        }
    }

    private StationEntity findOrSaveStation(String name) {
        return stationDao.findByName(name)
                .orElseGet(() -> stationDao.insert(new StationEntity(name)));
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        Map<Long, StationEntity> idByStations = stationDao.findAll().stream()
                .collect(toMap(StationEntity::getId, stationEntity -> stationEntity));
        Map<Long, List<SectionEntity>> idBySections = sectionDao.findAll().stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity ->
                        toLine(
                                lineEntity,
                                idBySections.getOrDefault(lineEntity.getId(), new ArrayList<>()),
                                idByStations
                        )
                )
                .collect(toList());
    }

    private Line toLine(LineEntity lineEntity, List<SectionEntity> sectionEntities,
                        Map<Long, StationEntity> idByStation) {
        List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        idByStation.get(sectionEntity.getSourceStationId()).getName(),
                        idByStation.get(sectionEntity.getTargetStationId()).getName(),
                        sectionEntity.getDistance()
                ))
                .collect(toList());

        return new Line(lineEntity.getName(), sections);
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("없습니다"));
        Map<Long, StationEntity> idByStations = stationDao.findByLineId(lineEntity.getId()).stream()
                .collect(toMap(StationEntity::getId, stationEntity -> stationEntity));

        return toLine(lineEntity, sectionDao.findByLineId(id), idByStations);
    }

    public void deleteById(Long id) {
        List<StationEntity> savedStations = stationDao.findAll();

        sectionDao.deleteByLineId(id);
        lineDao.deleteById(id);

        List<StationEntity> afterDeleteStations = stationDao.findAllBySections();

        savedStations.removeAll(afterDeleteStations);
        List<Long> collect = savedStations.stream()
                .map(StationEntity::getId)
                .collect(toList());
        stationDao.deleteByIds(collect);
    }

    public boolean existsByName(String lineName) {
        return lineDao.existsByName(lineName);
    }
}
