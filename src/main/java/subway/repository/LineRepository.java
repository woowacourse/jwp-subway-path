package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.line.LineDao;
import subway.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long insertLine(final LineEntity lineEntity) {
        return lineDao.insert(lineEntity);
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
