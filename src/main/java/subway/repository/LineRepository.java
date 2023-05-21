package subway.repository;

import java.util.List;
import subway.domain.Line;

public interface LineRepository {

    Long save(final Line line);

    Line findById(final Long id);

    List<Line> findAll();

    void update(final Long id, final Line line);

    void deleteById(final Long id);

    boolean notExistsById(final Long id);
}
