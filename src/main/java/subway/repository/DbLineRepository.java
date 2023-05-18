package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;

    public DbLineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public List<Line> findAllLines() {
        return lineDao.findAll().stream()
                .map(Line::from)
                .collect(Collectors.toList());
    }

    @Override
    public Line findById(final long id) {
        return Line.from(lineDao.findById(id));
    }

    @Override
    public Line save(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        return Line.from(lineDao.insert(lineEntity));
    }
}
