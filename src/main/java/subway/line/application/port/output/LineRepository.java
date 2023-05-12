package subway.line.application.port.output;

import subway.line.domain.Line;

import java.util.List;

public interface LineRepository {
    Line findById(Long id);

    List<Line> findAll();
}
