package subway.persistence.repository;

import java.util.Optional;
import subway.domain.Line;
import subway.domain.Lines;

public interface LineRepository {

    Long save(final Line line);

    void update(final Line line);

    Optional<Line> findById(final Long id);

    Optional<Line> findByName(final String name);

    Lines findAll();

    void delete(final Line line);
}
