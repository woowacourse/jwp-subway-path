package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineStationsResponse;
import subway.controller.dto.LinesResponse;

@Service
public class LineService {

    public Long createLine(final LineCreateRequest request) {
        return null;
    }

    public LineStationsResponse findLineById(final Long lineId) {
        return null;
    }

    public LinesResponse findLines() {
        return null;
    }
}
