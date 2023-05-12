package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;

public interface LineRepository {

    Line insert(Line line);

    List<Line> findAll();

    Line findById(Long id);

    void update(Line newLine);

    void deleteById(Long id);

    Optional<Line> findByName(String lineName);
}
