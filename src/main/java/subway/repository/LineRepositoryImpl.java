package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

@Repository
public class LineRepositoryImpl implements LineRepository {

    @Override
    public Line insert(final Line line) {
        return null;
    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public Line findById(final Long id) {
        return null;
    }

    @Override
    public void update(final Line newLine) {

    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public Optional<Line> findByName(final String lineName) {
        return Optional.empty();
    }
}
