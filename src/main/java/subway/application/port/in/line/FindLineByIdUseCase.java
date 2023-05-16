package subway.application.port.in.line;

import subway.application.port.in.line.dto.response.LineQueryResponse;

public interface FindLineByIdUseCase {

    LineQueryResponse findLineById(long lineId);
}
