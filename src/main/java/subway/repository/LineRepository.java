package subway.repository;

import java.util.Optional;
import subway.domain.Line;
import subway.domain.StationEdge;

public interface LineRepository {

    Optional<Line> findById(Long id);
    Optional<Line> findByName(String name);
    Long create(Line line);

    void updateWithSavedEdge(Line line, StationEdge stationEdge);
}
