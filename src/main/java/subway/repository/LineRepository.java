package subway.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
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

    public void save(final Line line) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(line.getName());
        if (lineEntity.isPresent()) {
            deleteAllByLineId(lineEntity.get());
        }
        final LineEntity newLine = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        final List<StationEntity> stations = line.findAllStation().stream()
                .map(station -> new StationEntity(station.getName(), newLine.getId()))
                .collect(toList());
        stationDao.insertAll(stations);
        final List<SectionEntity> sections = line.getSections().stream()
                .map(section -> new SectionEntity(
                        section.getStartName(),
                        section.getEndName(),
                        section.getDistanceValue(),
                        newLine.getId()
                ))
                .collect(toList());
        sectionDao.insertAll(sections);
    }

    private void deleteAllByLineId(final LineEntity lineEntity) {
        sectionDao.deleteAll(lineEntity.getId());
        stationDao.deleteByLineId(lineEntity.getId());
        lineDao.deleteById(lineEntity.getId());
    }
}
