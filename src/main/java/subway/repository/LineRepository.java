package subway.repository;

import static java.util.stream.Collectors.toList;

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
    private final LineMapper lineMapper;

    public LineRepository(
            final LineDao lineDao,
            final StationDao stationDao,
            final SectionDao sectionDao,
            final LineMapper lineMapper
    ) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineMapper = lineMapper;
    }

    public Line save(final Line line) {
        if (Objects.isNull(line.getId())) {
            return saveNewLine(line);
        }
        final Line savedLine = findById(line.getId()).orElseThrow(LineNotFoundException::new);
        lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor()));
        addNewStation(line);
        addNewSection(line);
        deleteRemovedSection(line, savedLine);
        deleteRemovedStation(line, savedLine);
        return findById(line.getId()).orElseThrow(LineNotFoundException::new);
    }

    private Line saveNewLine(final Line line) {
        final LineEntity lineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        final List<StationEntity> stationEntities = lineMapper.toStationEntities(line, lineEntity.getId());
        stationDao.insertAll(stationEntities);
        final List<StationEntity> savedStations = stationDao.findByLineId(lineEntity.getId());
        List<SectionEntity> sectionEntities = lineMapper.toSectionEntities(line, lineEntity.getId(), savedStations);
        sectionDao.insertAll(sectionEntities);
        return findById(lineEntity.getId()).orElseThrow(LineNotFoundException::new);
    }

    private void addNewStation(final Line line) {
        line.getSections().stream()
                .flatMap(section -> Stream.of(section.getStart(), section.getEnd()))
                .filter(station -> Objects.isNull(station.getId()))
                .map(Station::getName)
                .distinct()
                .map(name -> new StationEntity(name, line.getId()))
                .forEach(stationDao::insert);
    }

    private void addNewSection(final Line line) {
        final List<StationEntity> stationEntities = stationDao.findByLineId(line.getId());
        final List<SectionEntity> sectionEntities = lineMapper.toSectionEntities(line, line.getId(), stationEntities);
        sectionDao.insertAll(sectionEntities);
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
        final List<StationEntity> stationEntities = stationDao.findAll();
        final Map<Long, List<SectionEntity>> idBySections = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> lineMapper.toLine(
                        lineEntity,
                        idBySections.getOrDefault(lineEntity.getId(), new ArrayList<>()),
                        stationEntities)
                )
                .collect(toList());
    }

    public Optional<Long> findIdByName(final String name) {
        return lineDao.findByName(name).map(LineEntity::getId);
    }

    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id).map(lineEntity -> {
            final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
            final List<StationEntity> stationEntities = stationDao.findAll();
            return lineMapper.toLine(lineEntity, sectionEntities, stationEntities);
        });
    }

    public void deleteById(final Long id) {
        sectionDao.deleteAllByLineId(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
