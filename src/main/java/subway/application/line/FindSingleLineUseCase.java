package subway.application.line;

import subway.ui.dto.response.LineResponse;

public interface FindSingleLineUseCase {
    LineResponse findSingleLine(Long id);
}
