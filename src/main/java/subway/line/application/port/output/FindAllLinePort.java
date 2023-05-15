package subway.line.application.port.output;

import subway.line.domain.Line;

import java.util.Set;

public interface FindAllLinePort {
    Set<Line> findAll();
}
