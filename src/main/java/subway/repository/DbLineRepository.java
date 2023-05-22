package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.entity.LineEntity;
import subway.exeption.InvalidLineException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;

    public DbLineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public List<Line> findAllLines() {
        return lineDao.findAll().stream()
                .map(Line::from)
                .collect(Collectors.toList());
    }

    @Override
    public Line findById(final long id) {
        final Optional<LineEntity> line = lineDao.findById(id);
        if (line.isEmpty()) {
            throw new InvalidLineException("존재하는 노선의 아이디를 입력해 주세요!");
        }
        return Line.from(line.get());
    }

    @Override
    public Line save(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName(), line.getColor());
        return Line.from(lineDao.insert(lineEntity));
    }
}
