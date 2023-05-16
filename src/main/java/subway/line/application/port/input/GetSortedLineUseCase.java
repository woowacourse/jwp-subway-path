package subway.line.application.port.input;

import subway.line.dto.GetSortedLineResponse;

public interface GetSortedLineUseCase {
    GetSortedLineResponse getSortedLine(Long lineId);
}
