package subway.domain.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    Line createLine(Line line);

    void deleteById(Long lineIdRequest);

    List<Line> findAll();

    Line findById(Long lineIdRequest);
}
