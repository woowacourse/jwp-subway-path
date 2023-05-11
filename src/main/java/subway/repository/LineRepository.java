package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;
import subway.domain.StationEdge;

public interface LineRepository {

    Optional<Line> findById(Long id);

    List<Line> findAll();
    Optional<Line> findByName(String name);
    Long create(Line line);

    void updateWithSavedEdge(Line line, StationEdge stationEdge);

    void deleteStation(Line line, Long stationId);

    void deleteById(Long id);
}
