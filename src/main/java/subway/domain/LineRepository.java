package subway.domain;

import java.util.List;
import java.util.Optional;

public interface LineRepository {

    Long save(final Line line);

    void update(final Line line);

    void delete(final Line line);

    Optional<Line> findById(final Long id);

    Optional<Line> findByName(final String name);

    List<Line> findAll();
}
