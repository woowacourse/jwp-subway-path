package subway.domain.edge;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Edges {

    private final List<Edge> edges;

    public Edges(final List<Edge> edges) {
        this.edges = edges;
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

    public List<Edge> getEdges() {
        return edges;
    }
}