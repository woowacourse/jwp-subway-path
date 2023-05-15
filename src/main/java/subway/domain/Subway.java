package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Subway {

    private final Line line;
    private final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations;
    private final Station start;

    private Subway(final Line line, final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations,
                   final Station start) {
        this.line = line;
        this.stations = stations;
        this.start = start;
    }

    public static Subway of(final Line line, final List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = generateStations(sections);
        Station start = findStartStation(stations);
        return new Subway(line, stations, start);
    }

    private static SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> generateStations(final List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = new SimpleDirectedWeightedGraph<>(
                DefaultWeightedEdge.class);
        for (Section section : sections) {
            Station right = section.getRight();
            Station left = section.getLeft();

            stations.addVertex(right);
            stations.addVertex(left);
            stations.setEdgeWeight(stations.addEdge(left, right), section.getDistance());
        }
        return stations;
    }

    private static Station findStartStation(SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        Station station = getBaseStationForFindingStart(stations);
        while (station != null && !stations.incomingEdgesOf(station).isEmpty()) {
            station = stations.incomingEdgesOf(station)
                    .stream()
                    .map(stations::getEdgeSource)
                    .findFirst()
                    .orElse(null);
        }
        return station;
    }

    private static Station getBaseStationForFindingStart(SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        return stations.edgeSet()
                .stream()
                .map(stations::getEdgeSource)
                .findFirst()
                .orElse(null);
    }

    public boolean hasStation(final Station station) {
        return stations.containsVertex(station);
    }

    public boolean hasLeftSection(final Station station) {
        if (!hasStation(station)) {
            throw new IllegalArgumentException("아직 역이 노선에 없습니다.");
        }
        return !stations.incomingEdgesOf(station).isEmpty();
    }

    public boolean hasRightSection(final Station station) {
        if (!hasStation(station)) {
            throw new IllegalArgumentException("아직 역이 노선에 없습니다.");
        }
        return !stations.outgoingEdgesOf(station).isEmpty();
    }

    public Section findLeftSection(final Station station) {
        Set<DefaultWeightedEdge> edge = stations.incomingEdgesOf(station);
        validate(edge);
        return edge.stream()
                .map(x -> new Section(stations.getEdgeSource(x), station,
                        new Distance((int) stations.getEdgeWeight(x))))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("구간을 찾을 수 없습니다."));
    }

    public Section findRightSection(final Station station) {
        Set<DefaultWeightedEdge> edge = stations.outgoingEdgesOf(station);
        validate(edge);
        return edge.stream()
                .map(x -> new Section(station, stations.getEdgeTarget(x),
                        new Distance((int) stations.getEdgeWeight(x))))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("구간을 찾을 수 없습니다."));
    }

    private void validate(final Set<DefaultWeightedEdge> edge) {
        if (edge.size() != 1) {
            throw new IllegalArgumentException("구간을 찾을 수 없습니다.");
        }
    }

    public Station getStart() {
        return start;
    }

    public Line getLine() {
        return line;
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (isStationEmpty()) {
            return orderedStations;
        }
        Station station = new Station(start.getId(), start.getName());
        while (!stations.outgoingEdgesOf(station).isEmpty()) {
            orderedStations.add(station);
            Section rightSection = findRightSection(station);
            station = rightSection.getRight();
        }
        orderedStations.add(station);
        return orderedStations;
    }

    private boolean isStationEmpty() {
        return stations.edgeSet().isEmpty();
    }
}
