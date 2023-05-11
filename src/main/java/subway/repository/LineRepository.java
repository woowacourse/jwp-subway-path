package subway.repository;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.LineNotFoundException;

@Transactional
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
        if (lineEntity.isPresent()) {
            deleteAllByLineId(lineEntity.get().getId());
        }
        final LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        final List<StationEntity> stations = StationEntity.of(line, newLineEntity.getId());
        stationDao.insertAll(stations);
        final List<SectionEntity> sections = SectionEntity.of(line, newLineEntity.getId());
        sectionDao.insertAll(sections);
        return line;
    }

    private void deleteAllByLineId(final Long id) {
        sectionDao.deleteAll(id);
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
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

    public void deleteById(final Long id) {
        lineDao.findById(id).orElseThrow(LineNotFoundException::new);
        deleteAllByLineId(id);
    }

    public Long findIdByName(final String name) {
        return lineDao.findByName(name)
                .orElseThrow(LineNotFoundException::new)
                .getId();
    }

    public void updateNameAndColorById(final Long id, final String name, final String color) {
        lineDao.update(new LineEntity(id, name, color));
    }

    public Line findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id).orElseThrow(LineNotFoundException::new);
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
        return toLine(lineEntity, sectionEntities);
    }
}
