package subway.application.port.out.line;

import subway.domain.Line;

import java.util.List;
import java.util.Optional;

public interface LineQueryHandler {
    List<Line> findAll();

    Optional<Line> findLineById(Long lineIdRequest);

    List<Line> findLinesById(List<Long> lineIds);

    Optional<Line> findByName(final Line line);
}
