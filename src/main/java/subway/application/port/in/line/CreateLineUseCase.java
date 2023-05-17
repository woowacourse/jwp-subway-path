package subway.application.port.in.line;

import subway.adapter.in.web.line.dto.LineRequest;

public interface CreateLineUseCase {
    Long createLine(final LineRequest lineRequest);
}
