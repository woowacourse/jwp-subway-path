package subway.persistence.dao;

import subway.domain.line.Line;

import java.util.List;
import java.util.Optional;

public interface LineDao {

    Line insert(Line line);

    List<Line> findAll();

    Optional<Line> findById(Long id);

    void update(Line newLine);

    void deleteById(Long id);
}
