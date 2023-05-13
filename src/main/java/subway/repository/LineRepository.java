package subway.repository;

import subway.domain.Line;

import java.util.List;

public interface LineRepository {

    Line findById(long lineId);

    void updateSections(Line line);

    List<Line> findAll();

    Line insert(Line line);

    void update(Long id, Line line);

    void deleteById(Long id);
}
