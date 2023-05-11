package subway.application.line.usecase;

import subway.ui.dto.response.LineResponse;

public interface FindSingleLineUseCase {
    LineResponse findSingleLine(Long id);
}
