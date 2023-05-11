package subway.repository;

import static java.util.stream.Collectors.toList;

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
            deleteAllByLineId(lineEntity.get());
        }
        final LineEntity newLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        final List<StationEntity> stations = StationEntity.of(line, newLineEntity.getId());
        stationDao.insertAll(stations);
        final List<SectionEntity> sections = SectionEntity.of(line, newLineEntity.getId());
        sectionDao.insertAll(sections);
        return line;
    }

    private void deleteAllByLineId(final LineEntity lineEntity) {
        sectionDao.deleteAll(lineEntity.getId());
        stationDao.deleteByLineId(lineEntity.getId());
        lineDao.deleteById(lineEntity.getId());
    }

    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findAll();

        final Map<Long, List<SectionEntity>> lineIdBySections = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return lineIdBySections.entrySet().stream()
                .map(entry -> toLine(searchLineEntityByLineId(lineEntities, entry.getKey()), entry.getValue()))
                .collect(toList());
    }

    private LineEntity searchLineEntityByLineId(final List<LineEntity> lineEntities, final Long id) {
        return lineEntities.stream()
                .filter(lineEntity -> lineEntity.getId().equals(id))
                .findFirst()
                .get();
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

    public void delete(final Line line) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isPresent()) {
            deleteAllByLineId(lineEntity.get());
        }
    }
}
