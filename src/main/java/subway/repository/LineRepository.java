package subway.repository;

import java.util.List;
import subway.domain.Line;

public interface LineRepository {

    Line save(Line line);

    List<Line> findAll();

    Line update(Line line);

    void deleteById(long id);
}
