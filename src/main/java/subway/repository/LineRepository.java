package subway.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    List<Line> findAllLines();

    Line save(Line line);

    Line findById(long id);
}
