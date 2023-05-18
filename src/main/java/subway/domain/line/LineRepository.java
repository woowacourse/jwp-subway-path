package subway.domain.line;

import java.util.Optional;

public interface LineRepository {

    Line insert(Line line);

    Optional<Line> findByLineName(String lineName);

    void remove(Line line);
}
