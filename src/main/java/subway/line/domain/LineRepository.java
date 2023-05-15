package subway.line.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LineRepository {

    void save(final Line line);

    void update(final Line line);

    void delete(final Line line);

    Optional<Line> findById(final UUID id);

    Optional<Line> findByName(final String name);

    List<Line> findAll();
}
