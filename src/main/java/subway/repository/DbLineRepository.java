package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;

@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;

    public DbLineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Line findById(final long id) {
        return Line.from(lineDao.findById(id));
    }

    @Override
    public Line findByName(final String name) {
        return Line.from(lineDao.findByName(name));
    }

    @Override
    public Line save(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        return Line.from(lineDao.save(lineEntity));
    }
}
