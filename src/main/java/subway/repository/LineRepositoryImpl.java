package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.entity.LineEntity;

@Component
public class LineRepositoryImpl implements LineRepository {
    private final LineDao lineDao;

    public LineRepositoryImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line insert(final Line line) {
        LineEntity lineEntity = lineDao.insert(line);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor()))
                .collect(Collectors.toList());
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public void update(final Line line) {
        lineDao.update(line);
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
