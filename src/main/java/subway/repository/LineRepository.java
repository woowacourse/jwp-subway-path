package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.line.Line;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    Line update(Line line);

    Optional<Line> findByName(String lineName);

    void delete(Line line);

}
