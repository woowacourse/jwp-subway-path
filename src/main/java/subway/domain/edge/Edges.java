package subway.domain.edge;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    public Edges add(final Station existStation, final Station newStation, final DirectionStrategy strategy, final Distance distance) {

        return strategy.calculate(edges, existStation, newStation, distance);
    }

    public Edges remove(final Station station) {
        final List<Edge> targetEdges = edges.stream()
                .filter(edge -> edge.hasStation(station))
                .collect(Collectors.toList());
        validateRemoveStation(targetEdges);

        if (targetEdges.size() == 1) {
            edges.remove(targetEdges.get(0));
        }
        if (targetEdges.size() == 2) {
            final Edge edge1 = targetEdges.get(0);
            final Edge edge2 = targetEdges.get(1);

            final Edge newEdge = new Edge(edge1.getUpStation(), edge2.getDownStation(), new Distance(edge1.getDistanceValue() + edge2.getDistanceValue()));
            final int targetIndex = targetEdges.indexOf(edge1);
            edges.remove(edge1);
            edges.remove(edge2);
            edges.add(targetIndex, newEdge);
        }
        return new Edges(edges);
    }

    private void validateRemoveStation(final List<Edge> targetEdges) {
        if (targetEdges.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 해당 노선에 존재하지 않습니다.");
        }
        if (targetEdges.size() > 2) {
            throw new UnsupportedOperationException("해당 노선에 갈래길이 존재합니다. 확인해주세요.");
        }
    }

    public List<Edge> getEdges() {
        return new LinkedList<>(edges);
    }
}