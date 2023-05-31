package subway.domain.line;

import java.util.List;
import java.util.Optional;

public interface LineRepository {

    Line insert(Line line);

    Line findLineById(Long lineId);

    Optional<Line> findByLineName(String lineName);

    List<Line> findAllLines();

    void update(Line newLine);

    void deleteById(Long lineId);
}
