package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FeeCalculator {

    private final double distance;
    private final List<Station> way;

    private FeeCalculator(final double distance, final List<Station> way) {
        this.distance = distance;
        this.way = way;
    }

    public static FeeCalculator from(final Station start, final Station end, final List<Line> lines) {
        final List<Station> stations = integrateStations(lines);

        final var graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (final Station station : stations) {
            graph.addVertex(station);
        }

        addEdges(lines, graph);

        final var dijkPath = new DijkstraShortestPath<>(graph)
                .getPath(start, end);
        try {
            return new FeeCalculator(dijkPath.getWeight(), dijkPath.getVertexList());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }

    private static List<Station> integrateStations(final List<Line> lines) {
        return lines.stream()
                .map(Line::sortStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static void addEdges(final List<Line> lines, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(Line::getPaths)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> graph.setEdgeWeight(
                        graph.addEdge(entry.getKey(), entry.getValue().getNext()), entry.getValue().getDistance())
                );
    }

    public double getDistance() {
        return distance;
    }

    public List<Station> getWay() {
        return way;
    }
}
