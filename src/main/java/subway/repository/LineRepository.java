package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long insertLine(final Line line) {
        final LineEntity lineEntity = lineDao.findByLineNumber(line.getLineNumber());
        return lineDao.insert(lineEntity);
    }

    public Long findLineIdByLine(final Line line) {
        final LineEntity lineEntity = lineDao.findByLineNumber(line.getLineNumber());
        return lineEntity.getLineId();
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(lineEntity -> new Line(lineEntity.getLineNumber(), lineEntity.getName(), lineEntity.getColor()))
                .collect(Collectors.toList());
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
