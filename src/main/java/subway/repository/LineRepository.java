package subway.repository;

import subway.domain.Line;

public interface LineRepository {
    Line save(Line line);

    Line findById(long id);
}
