package subway.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.LineNotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line save(final Line line) {
        if (Objects.isNull(line.getId())) {
            return saveNewLine(line);
        }
        final Line savedLine = findById(line.getId()).orElseThrow(LineNotFoundException::new);
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
        addNewStation(line, savedLine);
        addNewSection(line);
        deleteRemovedSection(line, savedLine);
        deleteRemovedStation(line, savedLine);
        return findById(line.getId()).orElseThrow(LineNotFoundException::new);
    }

    private Line saveNewLine(final Line line) {
        final LineEntity lineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        final List<StationEntity> stations = line.findAllStation().stream()
                .map(station -> new StationEntity(station.getName(), lineEntity.getId()))
                .collect(toList());
        stationDao.insertAll(stations);
        final List<StationEntity> savedStations = stationDao.findByLineId(lineEntity.getId());
        final Map<String, Long> stationNameByStationId = savedStations.stream()
                .collect(toMap(StationEntity::getName, StationEntity::getId));
        final List<SectionEntity> sections = line.getSections().stream()
                .map(section -> new SectionEntity(
                        stationNameByStationId.get(section.getStartName()),
                        stationNameByStationId.get(section.getEndName()),
                        section.getDistanceValue(),
                        lineEntity.getId()
                ))
                .collect(toList());
        sectionDao.insertAll(sections);
        return findById(lineEntity.getId()).orElseThrow(LineNotFoundException::new);
    }

    private void addNewStation(final Line line, final Line savedLine) {
        line.getSections().stream()
                .flatMap(section -> Stream.of(section.getStart(), section.getEnd()))
                .filter(station -> Objects.isNull(station.getId()))
                .map(Station::getName)
                .distinct()
                .map(name -> new StationEntity(name, savedLine.getId()))
                .forEach(stationDao::insert);
    }

    private void addNewSection(final Line line) {
        final List<StationEntity> stations = stationDao.findByLineId(line.getId());
        final Map<String, Long> stationNameByStationId = stations.stream()
                .collect(toMap(StationEntity::getName, StationEntity::getId));

        final List<SectionEntity> sections = line.getSections().stream()
                .filter(section -> Objects.isNull(section.getId()))
                .map(section -> new SectionEntity(
                        stationNameByStationId.get(section.getStartName()),
                        stationNameByStationId.get(section.getEndName()),
                        section.getDistanceValue(),
                        line.getId()
                ))
                .collect(toList());
        sectionDao.insertAll(sections);
    }

    private void deleteRemovedSection(final Line line, final Line savedLine) {
        final List<Long> sectionIds = line.getSections().stream()
                .map(Section::getId)
                .collect(toList());
        savedLine.getSections().stream()
                .map(Section::getId)
                .filter(Objects::nonNull)
                .filter(id -> !sectionIds.contains(id))
                .forEach(sectionDao::deleteById);
    }

    private void deleteRemovedStation(final Line line, final Line savedLine) {
        final List<Long> stationIds = line.getSections().stream()
                .flatMap(section -> Stream.of(section.getStart(), section.getEnd()))
                .map(Station::getId)
                .collect(toList());
        savedLine.getSections().stream()
                .flatMap(section -> Stream.of(section.getStart(), section.getEnd()))
                .map(Station::getId)
                .filter(id -> !stationIds.contains(id))
                .forEach(stationDao::deleteById);
    }

    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        final List<StationEntity> stations = stationDao.findAll();
        final Map<Long, String> stationIdByStationName = stations.stream()
                .collect(toMap(StationEntity::getId, StationEntity::getName));

        final Map<Long, List<SectionEntity>> idBySections = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> toLine(
                        lineEntity,
                        idBySections.getOrDefault(lineEntity.getId(), new ArrayList<>()),
                        stationIdByStationName)
                )
                .collect(toList());
    }

    private Line toLine(
            final LineEntity lineEntity,
            final List<SectionEntity> sectionEntities,
            final Map<Long, String> stationIdByStationName
    ) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> toSection(stationIdByStationName, sectionEntity))
                .collect(toList());

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    private static Section toSection(
            final Map<Long, String> stationIdByStationName,
            final SectionEntity sectionEntity
    ) {
        return new Section(
                sectionEntity.getId(),
                new Station(sectionEntity.getStartStationId(),
                        stationIdByStationName.get(sectionEntity.getStartStationId())),
                new Station(sectionEntity.getEndStationId(),
                        stationIdByStationName.get(sectionEntity.getEndStationId())),
                new Distance(sectionEntity.getDistance())
        );
    }

    public Optional<Long> findIdByName(final String name) {
        return lineDao.findByName(name).map(LineEntity::getId);
    }

    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id).map(lineEntity -> {
            final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
            final List<StationEntity> stations = stationDao.findAll();
            final Map<Long, String> stationIdByStationName = stations.stream()
                    .collect(toMap(StationEntity::getId, StationEntity::getName));
            return toLine(lineEntity, sectionEntities, stationIdByStationName);
        });
    }

    public void deleteById(final Long id) {
        sectionDao.deleteAllByLineId(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }

    public void delete(final Long id) {
        sectionDao.deleteAllByLineId(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
