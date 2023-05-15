package subway.domain.edge;

import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DownDirectionStrategy implements DirectionStrategy {

    @Override
    public Edges calculate(final List<Edge> edges, final Station existStation, final Station newStation, final Distance distance) {
        final Optional<Edge> existEdgeOptional = edges.stream()
                .filter(edge -> edge.getUpStation().equals(existStation))
                .findFirst();

        if (existEdgeOptional.isPresent()) {
            final Edge existEdge = existEdgeOptional.get();
            if (existEdge.getDistance().isLowerOrEqualThan(distance)) {
                throw new InvalidDistanceException(distance.getDistance(), existEdge.getDistanceValue());
            }
            final Edge edge1 = new Edge(newStation, existEdge.getDownStation(), existEdge.getDistance().minus(distance));
            final Edge edge2 = new Edge(existStation, newStation, distance);
            final int existIndex = edges.indexOf(existEdge);
            edges.remove(existIndex);
            edges.add(existIndex, edge1);
            edges.add(existIndex, edge2);
        }
        if (existEdgeOptional.isEmpty()) {
            edges.add(new Edge(existStation, newStation, distance));
        }

        return new Edges(new LinkedList<>(edges));
    }
}
