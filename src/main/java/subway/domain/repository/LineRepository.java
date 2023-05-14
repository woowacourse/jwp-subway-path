package subway.domain.repository;

import subway.domain.Line;

import java.util.List;
import java.util.Optional;

public interface LineRepository {
    Long createLine(Line line);

    void deleteById(Long lineIdRequest);

    List<Line> findAll();

    Line findById(Long lineIdRequest);

    Optional<Line> findByName(final Line line);
}
