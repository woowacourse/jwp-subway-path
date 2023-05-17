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

    private SubwayGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.subwayGraph = graph;
    }

    public static SubwayGraph from(Lines lines) {
        List<Station> stations = lines.getAllStations();
        List<Path> paths = lines.findPath();
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        paths.forEach(path -> graph.addEdge(path.getSource(), path.getTarget(), path));
        return new SubwayGraph(graph);
    }

    public List<Station> findPath(Station source, Station sink) {
        return shortestPath(source, sink).getVertexList();
    }

    public int calculateDistanceSum(Station source, Station sink) {
        return calculateDistance(source, sink).stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private List<Integer> calculateDistance(Station source, Station sink) {
        List<DefaultWeightedEdge> edgeList = shortestPath(source, sink).getEdgeList();
        return mapToWeight(edgeList);
    }

    private List<Integer> mapToWeight(List<DefaultWeightedEdge> edgeList) {
        return edgeList.stream()
                .map(edge -> ((Path) edge).getWeight())
                .map(Double::intValue)
                .collect(Collectors.toList());
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestPath(Station source, Station sink) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayGraph);
        return dijkstraShortestPath.getPath(source, sink);
    }
}
