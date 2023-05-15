package subway.line.application.port.output;

import subway.line.domain.Line;

public interface GetLineByIdPort {
    Line getLineById(final Long id);
}
