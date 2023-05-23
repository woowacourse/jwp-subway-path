package subway.domain.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {
    Long save(Line line);

    List<Line> findAll();

    Line findLineById(Long id);

    Long findIdByName(String name);
}
