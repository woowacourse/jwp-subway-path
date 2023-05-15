package subway.line.application.port.output;

import subway.line.domain.Line;

public interface SaveLinePort {
    Long save(Line line);
}
