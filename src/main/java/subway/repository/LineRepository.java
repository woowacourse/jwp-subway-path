package subway.repository;

import java.util.List;
import subway.domain.line.Line;


public interface LineRepository {
    Line insert(final Line line);

    List<Line> findAll();

    Line findById(final Long id);

    void update(final Line line);

    void deleteById(final Long id);
}
