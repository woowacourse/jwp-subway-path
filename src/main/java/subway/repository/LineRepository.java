package subway.repository;

import subway.domain.line.Line;

import java.util.List;
import java.util.Optional;

public interface LineRepository {

    Optional<Line> findById(final Long id);

    List<Line> findAll();

    Optional<Line> findByName(final String name);

    Long create(final Line line);

    void updateStationEdges(final Line line);

    void deleteById(final Long id);
}
