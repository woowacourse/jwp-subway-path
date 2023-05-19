package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Navigation {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private Navigation(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static Navigation from(final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initialize(sections, graph);
        return new Navigation(graph);
    }

    private static void initialize(final Sections sections, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections.get()) {
            final Station source = section.getSource();
            final Station target = section.getTarget();
            graph.addVertex(source);
            graph.addVertex(target);
            graph.setEdgeWeight(graph.addEdge(source, target), section.getDistance());
        }
    }

    public List<Station> getShortestPath(final Station source, final Station target) {
        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
