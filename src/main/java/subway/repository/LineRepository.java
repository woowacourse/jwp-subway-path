package subway.repository;

import java.util.Optional;
import subway.domain.Line;

public interface LineRepository {
    Optional<Line> findByName(String name);
    Long create(Line line);
}
