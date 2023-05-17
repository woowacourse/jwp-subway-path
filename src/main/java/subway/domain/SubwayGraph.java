package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public List<Station> findPath(Station source, Station sink, Lines lines) {
        List<Station> stations = lines.getAllStations();
        List<Path> paths = lines.findPath();
        GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPath(source, sink, stations, paths);
        return graphPath.getVertexList();
    }

    private GraphPath<Station, DefaultWeightedEdge> shortestPath(Station source, Station sink, List<Station> stations, List<Path> paths) {
        stations.forEach(graph::addVertex);
        paths.forEach(path -> graph.addEdge(path.getSource(), path.getTarget(), path));

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, sink);
    }
}
