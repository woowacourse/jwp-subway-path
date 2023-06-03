package subway.persistence.repository.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.exception.notfound.LineNotFoundException;
import subway.persistence.dao.line.LineDao;
import subway.persistence.entity.line.LineEntity;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;

    public JdbcLineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Long save(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getLineNumber(), line.getName(), line.getColor(), line.getAdditionalFare());
        return lineDao.save(lineEntity);
    }

    @Override
    public void deleteById(final Long id) {
        final boolean exist = lineDao.isIdExist(id);
        if (exist) {
            lineDao.deleteByLineId(id);
            return;
        }
        throw new LineNotFoundException();
    }

    @Override
    public Line findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id);
        return new Line(lineEntity.getLineId(),
                lineEntity.getLineNumber(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getAdditionalFare());
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(lineEntity -> new Line(lineEntity.getLineId(),
                        lineEntity.getLineNumber(),
                        lineEntity.getName(),
                        lineEntity.getColor(),
                        lineEntity.getAdditionalFare()))
                .collect(Collectors.toList());
    }
}
