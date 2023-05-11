package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line save(final Line line) {
        final LineEntity entity = lineDao.save(new LineEntity(line.getId(), line.getName(), line.getColor()));
        return Line.from(entity);
    }

    public List<Line> findAll() {
        return null;
    }

    public Line findById(final Long lineId) {
        return null;
    }

    public void update(final Line line) {

    }
}
