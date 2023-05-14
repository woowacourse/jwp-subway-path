package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.persistence.dao.LineDao;
import subway.persistence.entity.LineEntity;

import java.util.List;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    public LineRepositoryImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Line createLine(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName());

        final Long lineId = lineDao.createLine(lineEntity);

        return new Line(lineId, lineEntity.getName());
    }

    @Override
    public void deleteById(final Long lineIdRequest) {

    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public Line findById(final Long lineIdRequest) {
        return null;
    }
}
