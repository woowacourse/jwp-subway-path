package subway.application.line.port.out;

import java.util.List;
import java.util.Optional;
import subway.domain.line.Line;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    Line update(Line line);

    Optional<Line> findById(final long id);

    void deleteById(final long id);
}
