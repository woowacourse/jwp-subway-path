package subway.line.application.port.input;

import subway.line.dto.AddLineRequest;

public interface AddLineUseCase {
    Long addLine(final AddLineRequest addLineRequest);
}
