package subway.service;

import subway.dto.LineRequest;
import subway.dto.LineResponse;

public interface LineService {
    LineResponse createLine(LineRequest lineRequest);
}
