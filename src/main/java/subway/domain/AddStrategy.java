package subway.domain;

import java.util.List;
import java.util.Optional;

public interface AddStrategy {
    void activate(List<Edge> edges, Station upStation, Station downStation, int distance);

    default Optional<Edge> findSectionByStationExistsAtDirection(
            final List<Edge> edges,
            final Station station,
            final Direction direction
    ) {
        return edges.stream()
                .filter(edge -> edge.isStationExistsAtDirection(station, direction))
                .findFirst();
    }
}
