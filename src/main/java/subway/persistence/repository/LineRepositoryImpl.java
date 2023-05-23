package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.repository.LineRepository;
import subway.persistence.dao.LineDao;
import subway.persistence.entity.LineEntity;
import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepositoryImpl implements LineRepository {
    private final LineDao lineDao;

    public LineRepositoryImpl(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Long save(Line line) {
        return lineDao.insert(new LineEntity(line.getName()));
    }

    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(this::toLine)
                .collect(Collectors.toList());
    }

    private Line toLine(LineEntity lineEntity) {
        return new Line(lineEntity.getName());
    }

    @Override
    public Line findLineById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);

        return toLine(lineEntity);
    }

    @Override
    public Long findIdByName(String name) {
        return lineDao.findIdByName(name);
    }
}
