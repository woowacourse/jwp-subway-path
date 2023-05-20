package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath;

    public SubwayGraph(final List<Section> sections) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(makeGraph(sections));
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> makeGraph(final List<Section> sections) {
        sections.forEach(
                section -> {
                    graph.addVertex(section.getUpStation().getName());
                    graph.addVertex(section.getDownStation().getName());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
                }
        );
        return graph;
    }

    public List<String> getDijkstraShortestPath(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPath(upStation.getName(), downStation.getName()).getVertexList();
    }

    public int getShortestPathWeight(final Station upStation, final Station downStation) {
        return (int) dijkstraShortestPath.getPathWeight(upStation.getName(), downStation.getName());
    }
}
