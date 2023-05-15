package subway.domain.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    long createLine(Line line);

    boolean deleteById(Long lineIdRequest);

    List<Line> findAll();

    Line findById(Long lineIdRequest);

    boolean updateLine(long lineId, Line line);

    Line findByName(String lineName);
}
