package subway.service.line;

import subway.service.line.domain.Line;

public interface LineRepository {
    Line insert(Line line);

    Line findById(long lineId);
}
