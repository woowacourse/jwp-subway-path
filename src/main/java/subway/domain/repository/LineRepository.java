package subway.domain.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    long createLine(Line line);

    boolean deleteById(long lineId);

    List<Line> findAll();

    Line findById(long lineId);

    boolean updateLine(long lineId, Line line);
}
