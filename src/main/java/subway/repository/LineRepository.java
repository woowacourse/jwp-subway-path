package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;
import subway.exception.notfound.LineNotFoundException;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long save(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getLineNumber(), line.getName(), line.getColor());
        return lineDao.save(lineEntity);
    }

    public void deleteById(final Long id) {
        final boolean exist = lineDao.isIdExist(id);
        if (exist) {
            lineDao.deleteByLineId(id);
            return;
        }
        throw new LineNotFoundException();
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(lineEntity -> new Line(lineEntity.getLineId(), lineEntity.getLineNumber(), lineEntity.getName(), lineEntity.getColor()))
                .collect(Collectors.toList());
    }
}
