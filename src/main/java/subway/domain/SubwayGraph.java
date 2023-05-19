package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath;

    public SubwayGraph() {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public void makeGraph(final List<Section> sections) {
        sections.forEach(
                section -> {
                    graph.addVertex(section.getUpStation().getName());
                    graph.addVertex(section.getDownStation().getName());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
                }
        );
    }

    public List<String> getDijkstraShortestPath(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPath(upStation.getName(), downStation.getName()).getVertexList();
    }

    public double getShortestPathWeight(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPathWeight(upStation.getName(), downStation.getName());
    }
}
