package subway.repository;

import subway.domain.Line;

public interface LineRepository {
    Line findById(long id);

    Line save(Line line);
}
