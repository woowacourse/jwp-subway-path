package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.repository.dao.LineDao;
import subway.repository.dao.LineSectionStationJoinDto;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    public LineRepositoryImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Line save(final Line line) {
        return lineDao.insert(line);
    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public Line findById(final Long id) {
        return lineDao.findById(id);
    }

    @Override
    public void update(final Line newLine) {

    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public Optional<Line> findByName(final String name) {
        final List<LineSectionStationJoinDto> joinDto = lineDao.findByName(name);
        final Line line = new LineConverter().convertToLine(joinDto);
        if (line == null) {
            return Optional.empty();
        }
        return Optional.of(line);
    }
}
