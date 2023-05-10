package subway.domain;

import java.util.List;
import java.util.Set;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

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
        StationConnections stationConnections = StationConnections.from(sections);
        Station start = stationConnections.findStartStation();

        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = new SimpleDirectedWeightedGraph<>(
                DefaultWeightedEdge.class);
        addVertex(stationConnections, stations);
        addEdge(sections, stations);
        return new Subway(line, stations, start);
    }

    private static void addVertex(final StationConnections stationConnections,
                                  final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Station station : stationConnections.getAllStations()) {
            stations.addVertex(station);
        }
    }

    private static void addEdge(final List<Section> sections,
                                final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Section section : sections) {
            stations.setEdgeWeight(
                    stations.addEdge(section.getLeft(), section.getRight()), section.getDistance());
        }
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

    public Line getLine() {
        return line;
    }

    public SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> getStations() {
        return stations;
    }
}
