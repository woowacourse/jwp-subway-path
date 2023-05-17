package subway.domain.graph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Lines;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph;

    private SubwayGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.subwayGraph = graph;
    }

    public static SubwayGraph from(final Lines lines) {
        final List<Station> stations = lines.getAllStations();
        final List<Path> paths = lines.findPath();
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        paths.forEach(path -> graph.addEdge(path.getSource(), path.getTarget(), path));
        return new SubwayGraph(graph);
    }

    public List<Station> findPath(final Station source, final Station sink) {
        return shortestPath(source, sink).getVertexList();
    }

    public int calculateDistanceSum(final Station source, final Station sink) {
        return calculateDistance(source, sink).stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private List<Integer> calculateDistance(final Station source, final Station sink) {
        final List<DefaultWeightedEdge> edgeList = shortestPath(source, sink).getEdgeList();
        return mapToWeight(edgeList);
    }

    private List<Integer> mapToWeight(final List<DefaultWeightedEdge> edgeList) {
        return edgeList.stream()
                .map(edge -> ((Path) edge).getWeight())
                .map(Double::intValue)
                .collect(Collectors.toList());
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestPath(final Station source, final Station sink) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayGraph);
        return dijkstraShortestPath.getPath(source, sink);
    }
}
