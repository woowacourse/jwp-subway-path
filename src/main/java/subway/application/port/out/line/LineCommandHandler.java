package subway.application.port.out.line;

import subway.domain.Line;

public interface LineCommandHandler {
    Long createLine(Line line);
    void deleteById(Long lineIdRequest);
}
