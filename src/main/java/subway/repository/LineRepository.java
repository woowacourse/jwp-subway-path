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

    public Long findLineIdByLine(final Line line) {
        final boolean exist = lineDao.isLineNumberExist(line.getLineNumber());
        if (exist) {
            final LineEntity lineEntity = lineDao.findByLineNumber(line.getLineNumber());
            return lineEntity.getLineId();
        }
        throw new LineNotFoundException();
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(lineEntity -> new Line(lineEntity.getLineNumber(), lineEntity.getName(), lineEntity.getColor()))
                .collect(Collectors.toList());
    }

    public void deleteByLineId(final Long lineId) {
        final boolean exist = lineDao.isLineIdExist(lineId);
        if (exist) {
            lineDao.deleteByLineId(lineId);
            return;
        }
        throw new LineNotFoundException();
    }
}
