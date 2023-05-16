package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.Line;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long insert(Line line) {
        return lineDao.insert(toEntity(line));
    }

    private LineEntity toEntity(Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        final List<LineEntity> entities = lineDao.findAll();
        return entities.stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }

    private Line mapFrom(LineEntity entity) {
        return new Line(entity.getId(), entity.getName(), entity.getColor());
    }

    public Line findById(Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 노선을 찾을 수 없습니다."));
        return mapFrom(lineEntity);
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }

    public boolean exists(Long lineId) {
        return lineDao.exists(lineId);
    }
}
