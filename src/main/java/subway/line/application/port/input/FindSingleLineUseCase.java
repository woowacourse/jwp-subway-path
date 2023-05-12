package subway.line.application.port.input;

import subway.ui.dto.response.LineResponse;

public interface FindSingleLineUseCase {
    LineResponse findSingleLine(Long id);
}
