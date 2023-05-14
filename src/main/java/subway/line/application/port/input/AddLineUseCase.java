package subway.line.application.port.input;

import subway.line.dto.LineSaveRequest;

public interface AddLineUseCase {
    Long addLine(final LineSaveRequest lineSaveRequest);
}
