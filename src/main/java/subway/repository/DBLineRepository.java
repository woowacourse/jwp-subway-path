package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.entity.LineEntity;

import java.util.Optional;

@Repository
public class DBLineRepository implements LineRepository {

    private final LineDao lineDao;

    public DBLineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Line insert(Line line) {
        LineEntity lineEntity = new LineEntity(null, line.getName());
        LineEntity insertedLineEntity = lineDao.insert(lineEntity);
        return new Line(insertedLineEntity.getId(), insertedLineEntity.getLineName());
    }

    @Override
    public Optional<Line> findByLineName(String lineName) {
        Optional<LineEntity> nullableLineEntity = lineDao.findByLineName(lineName);
        if (nullableLineEntity.isEmpty()) {
            return Optional.empty();
        }
        LineEntity findLineEntity = nullableLineEntity.get();
        return Optional.of(new Line(findLineEntity.getId(), findLineEntity.getLineName()));
    }

    @Override
    public void remove(Line line) {
        lineDao.deleteById(line.getId());
    }
}
