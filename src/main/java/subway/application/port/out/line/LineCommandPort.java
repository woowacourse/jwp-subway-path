package subway.application.port.out.line;

import subway.domain.Line;

public interface LineCommandPort {
    Long createLine(Line line);
    void deleteById(Long lineIdRequest);
}
