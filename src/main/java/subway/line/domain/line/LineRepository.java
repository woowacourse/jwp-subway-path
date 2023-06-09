package subway.line.domain.line;

import java.util.List;
import java.util.Optional;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    void update(Line line);

    Optional<Line> findById(long id);

    void deleteById(long id);
}
