package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Line;
import subway.domain.StationEdge;

public interface LineRepository {

    Optional<Line> findById(final Long id);

    List<Line> findAll();

    Optional<Line> findByName(final String name);

    Long create(final Line line);

    Long insertStationEdge(final Line line, final StationEdge stationEdge);

    void updateStationEdge(final Line line, final StationEdge stationEdge);

    void deleteStation(final Line line, final Long stationId);

    void deleteById(final Long id);
}
