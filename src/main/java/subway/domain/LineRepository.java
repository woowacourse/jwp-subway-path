package subway.domain;

import java.util.List;
import java.util.Optional;

public interface LineRepository {

    Optional<Line> findById(Long id);

    List<Line> findAll();

    Line save(Line line);

    void delete(Line line);

}
