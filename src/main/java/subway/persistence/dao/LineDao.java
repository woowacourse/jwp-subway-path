package subway.persistence.dao;

import subway.domain.Line;

import java.util.List;

public interface LineDao {

    Line insert(Line line);

    List<Line> findAll();

    Line findById(Long id);

    void update(Line newLine);

    void deleteById(Long id);
}
