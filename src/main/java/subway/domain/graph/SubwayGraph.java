package subway.domain.graph;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Lines;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph {
    private WeightedGraph<Station, DefaultWeightedEdge> subwayGraph;
    private ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPath;

    public SubwayGraph(final WeightedGraph<Station, DefaultWeightedEdge> graph) {
        this.subwayGraph = graph;
    }

    public static SubwayGraph from(final Lines lines) {
        final List<Station> stations = lines.getAllStations();
        final Paths paths = Paths.from(lines);

        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        paths.addAllToGraph(graph);
        return new SubwayGraph(graph);
    }

    public void update(final Lines lines) {
        final List<Station> stations = lines.getAllStations();
        final Paths paths = Paths.from(lines);

        final WeightedGraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        paths.addAllToGraph(graph);

        this.subwayGraph = graph;
        this.shortestPath = new DijkstraShortestPath<>(subwayGraph);
    }

    public List<Station> findPath(final Station source, final Station sink) {
        if (shortestPath == null) {
            shortestPath = new DijkstraShortestPath<>(subwayGraph);
        }
        return shortestPath.getPath(source, sink).getVertexList();
    }

    public int calculateDistanceSum(final Station source, final Station sink) {
        return calculateDistance(source, sink).stream()
                .mapToInt(Double::intValue)
                .sum();
    }

    private List<Double> calculateDistance(final Station source, final Station sink) {
        final List<DefaultWeightedEdge> edgeList = shortestPath.getPath(source, sink).getEdgeList();
        return mapToWeight(edgeList);
    }

    private List<Double> mapToWeight(final List<DefaultWeightedEdge> edgeList) {
        return edgeList.stream()
                .map(edge -> ((Path) edge).getWeight())
                .collect(Collectors.toList());
    }
}
