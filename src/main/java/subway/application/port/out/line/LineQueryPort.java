package subway.application.port.out.line;

import subway.domain.Line;

import java.util.List;
import java.util.Optional;

public interface LineQueryPort {
    List<Line> findAll();

    Optional<Line> findById(Long lineIdRequest);

    Optional<Line> findByName(final Line line);
}
