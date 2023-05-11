package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;

public interface LineRepository {

    Optional<Line> findById(long lineId);

    void updateSections(Line line);

    List<Line> findAll();
}
