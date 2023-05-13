package subway.repository;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
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
        final Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isEmpty()) {
            final LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
            return saveLine(line, newLineEntity);
        }
        final LineEntity updateLineEntity = lineEntity.orElseThrow(LineNotFoundException::new);
        sectionDao.deleteAll(updateLineEntity.getId());
        stationDao.deleteByLineId(updateLineEntity.getId());
        lineDao.update(updateLineEntity);
        return saveLine(line, updateLineEntity);
    }

    private Line saveLine(final Line line, final LineEntity lineEntity) {
        final List<StationEntity> stations = StationEntity.of(line, lineEntity.getId());
        stationDao.insertAll(stations);
        final List<SectionEntity> sections = SectionEntity.of(line, lineEntity.getId());
        sectionDao.insertAll(sections);
        return line;
    }

    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findAll();

        final Map<Long, List<SectionEntity>> idBySections = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, idBySections.getOrDefault(lineEntity.getId(), new ArrayList<>())))
                .collect(toList());
    }

    private Line toLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getStartStationName(),
                        sectionEntity.getEndStationName(),
                        sectionEntity.getDistance()
                ))
                .collect(toList());

        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Optional<Long> findIdByName(final String name) {
        return lineDao.findByName(name).map(LineEntity::getId);
    }

    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id).map(lineEntity -> {
            final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
            return toLine(lineEntity, sectionEntities);
        });
    }

    public void updateNameAndColorById(final Long id, final String name, final String color) {
        lineDao.update(new LineEntity(id, name, color));
    }

    public void deleteById(final Long id) {
        sectionDao.deleteAll(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
