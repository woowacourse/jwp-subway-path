package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;

public class JGraphPath {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public JGraphPath(Set<Station> vertex, List<Section> edges) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initializeGraph(vertex, edges);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void initializeGraph(Set<Station> vertex, List<Section> edges) {
        vertex.forEach(graph::addVertex);
        edges.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()
                )
        );
    }

    public List<Station> findPath(Station startStation, Station endStation) {
        return dijkstraShortestPath.getPath(startStation, endStation)
                .getVertexList();
    }

    public int findShortestDistance(Station startStation, Station endStation) {
        return (int) dijkstraShortestPath.getPathWeight(startStation, endStation);
    }
}
