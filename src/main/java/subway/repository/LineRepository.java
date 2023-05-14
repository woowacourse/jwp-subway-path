package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    Optional<Line> findById(Long id);

    void update(Line line);

    void deleteById(Long id);

    Optional<Line> findByName(String lineName);
}
