package subway.domain.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    long createLine(Line line);

    boolean deleteById(Long lineId);

    List<Line> findAll();

    Line findById(Long lineId);

    boolean updateLine(long lineId, Line line);

    Line findByName(String lineName);
}
