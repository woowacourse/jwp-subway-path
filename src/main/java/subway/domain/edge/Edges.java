package subway.domain.edge;

import subway.domain.line.Direction;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Edges {

    private final LinkedList<Edge> edges;

    public Edges(final List<Edge> edges) {
        this.edges = new LinkedList<>(edges);
    }

    public List<Station> getStations() {
        final Map<Station, Station> stationToStation = edges.stream()
                .collect(Collectors.toMap(Edge::getUpStation, Edge::getDownStation));
        final Set<Station> upStations = new HashSet<>(stationToStation.keySet());
        upStations.removeAll(stationToStation.values());

        final List<Station> result = new ArrayList<>(upStations);
        Station targetStation = result.get(0);
        while (stationToStation.containsKey(targetStation)) {
            final Station next = stationToStation.get(targetStation);
            result.add(next);
            targetStation = next;
        }
        return result;
    }

    public Edges add(final Station existStation, final Station newStation, final Direction direction, final Integer distance) {
        final Optional<Edge> existEdgeOptional = findTargetEdge(existStation, direction);
        if (direction == Direction.UP) {
            if (existEdgeOptional.isPresent()) {
                final Edge existEdge = existEdgeOptional.get();
                if (existEdge.getDistance() <= distance) {
                    throw new IllegalArgumentException("추가하려는 구간의 길이가 기존 구간의 길이보다 깁니다.");
                }
                final Edge edge1 = new Edge(existEdge.getUpStation(), newStation, existEdge.getDistance() - distance);
                final Edge edge2 = new Edge(newStation, existStation, distance);
                final int existIndex = edges.indexOf(existEdge);
                edges.remove(existIndex);
                edges.add(existIndex, edge2);
                edges.add(existIndex, edge1);
            }
            if (existEdgeOptional.isEmpty()) {
                edges.add(0, new Edge(newStation, existStation, distance));
            }
            return new Edges(new LinkedList<>(edges));
        }
        if (direction == Direction.DOWN) {
            if (existEdgeOptional.isPresent()) {
                final Edge existEdge = existEdgeOptional.get();
                if (existEdge.getDistance() <= distance) {
                    throw new IllegalArgumentException("추가하려는 구간의 길이가 기존 구간의 길이보다 깁니다.");
                }
                final Edge edge1 = new Edge(newStation, existEdge.getDownStation(), existEdge.getDistance() - distance);
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
        throw new UnsupportedOperationException();
    }

    public Optional<Edge> findTargetEdge(final Station existStation, final Direction direction) {
        if (direction == Direction.UP) {
            return edges.stream()
                    .filter(edge -> edge.getDownStation().equals(existStation))
                    .findFirst();
        }
        if (direction == Direction.DOWN) {
            return edges.stream()
                    .filter(edge -> edge.getUpStation().equals(existStation))
                    .findFirst();
        }
        throw new UnsupportedOperationException();
    }

    public List<Edge> getEdges() {
        return new LinkedList<>(edges);
    }
}