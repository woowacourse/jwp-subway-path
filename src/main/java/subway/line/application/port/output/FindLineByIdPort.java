package subway.line.application.port.output;

import subway.line.domain.Line;

public interface FindLineByIdPort {
    Line findById(final Long id);
}
