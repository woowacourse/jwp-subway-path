package subway.line.application.port.out;

import java.util.List;
import java.util.Optional;
import subway.line.domain.Line;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    Line update(Line line);

    Optional<Line> findById(long id);

    void deleteById(long id);
}
