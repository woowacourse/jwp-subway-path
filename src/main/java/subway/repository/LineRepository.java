package subway.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.DuplicatedNameException;
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
        final String lineName = line.getName();
        validateDuplicatedLineName(lineName);

        LineEntity savedLineEntity = lineDao.insert(new LineEntity(lineName));

        List<StationEntity> stations = StationEntity.of(line);
        stationDao.insertAll(stations);

        saveSections(savedLineEntity.getId(), line);

        return savedLineEntity.getId();
    }

    private void validateDuplicatedLineName(final String lineName) {
        if (lineDao.existsByName(lineName)) {
            throw new DuplicatedNameException(lineName);
        }
    }

    private void saveSections(Long lineId, Line line) {
        for (Section section : line.getSections()) {
            StationEntity sourceStationEntity = toEntity(section.getSource());
            StationEntity targetStationEntity = toEntity(section.getTarget());

            sectionDao.insert(
                    new SectionEntity(sourceStationEntity.getId(), targetStationEntity.getId(),
                            lineId,
                            section.getDistance())
            );
        }
    }

    private StationEntity toEntity(final Station station) {
        return stationDao.findByName(station.getName())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역 이름입니다."));
    }

    public Line findById(Long id) {
        Subway subway = new Subway(findAllLine());
        LineEntity lineEntity = lineDao.findById(id);
        return subway.findLineByName(lineEntity.getName());
    }

    public List<Line> findAllLine() {
        List<LineEntity> lineEntities = lineDao.findAll();

        Map<Long, StationEntity> stationEntityById = stationDao.findAll().stream()
                .collect(toMap(StationEntity::getId, stationEntity -> stationEntity));

        Map<Long, List<SectionEntity>> sectionEntitiesByLineId = sectionDao.findAll().stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> toLine(
                        lineEntity,
                        sectionEntitiesByLineId.getOrDefault(lineEntity.getId(), new ArrayList<>()),
                        stationEntityById))
                .collect(toList());
    }

    private Line toLine(final LineEntity lineEntity,
                        final List<SectionEntity> sectionEntities,
                        final Map<Long, StationEntity> stationEntityById) {

        List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        stationEntityById.get(sectionEntity.getSourceStationId()).getName(),
                        stationEntityById.get(sectionEntity.getTargetStationId()).getName(),
                        sectionEntity.getDistance()
                ))
                .collect(toList());

        return new Line(lineEntity.getName(), sections);
    }

    public void updateLine(Long lineId, Line updatedLine) {
        sectionDao.deleteAll(lineId);
        saveSections(lineId, updatedLine);
    }
}
