package subway.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;

@RequiredArgsConstructor
@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    @Override
    public Line save(final Line line) {
        final LineEntity entity = LineEntity.from(line);
        final LineEntity lineEntity = lineDao.insert(entity);
        return new Line(lineEntity.getName(), lineEntity.getColor());
    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public Line update(final Line line) {
        return null;
    }

    @Override
    public void deleteById(final long id) {

    }
}
