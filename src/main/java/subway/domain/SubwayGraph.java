package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public SubwayGraph(final List<Section> sections) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(createGraph(sections));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(final List<Section> sections) {
        sections.forEach(this::addElement);
        return graph;
    }

    private void addElement(final Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getValue());
    }

    public List<Station> getPath(final Station source, final Station destination) {
        validateVertex(source, destination);
        return dijkstraShortestPath.getPath(source, destination).getVertexList();
    }

    public int getWeight(final Station source, final Station destination) {
        validateVertex(source, destination);
        return (int) dijkstraShortestPath.getPathWeight(source, destination);
    }

    private void validateVertex(final Station source, final Station destination) {
        if (!graph.containsVertex(source) || !graph.containsVertex(destination)) {
            throw new IllegalArgumentException("그래프에 역이 존재하지 않습니다.");
        }
    }
}
