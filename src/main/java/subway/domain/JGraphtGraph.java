package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JGraphtGraph implements Graph {

    private final WeightedGraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    @Override
    public void setGraph(final Sections sections) {
        for (Section section : sections.copySections()) {
            graph.addVertex(section.getUpStation().getName());
            graph.addVertex(section.getDownStation().getName());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
        }
    }

    @Override
    public Long findShortestDistance(String upStation, String downStation) {
        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<String, DefaultWeightedEdge> path = shortestPath.getPath(upStation, downStation);
        return (long) path.getWeight();
    }

    @Override
    public List<String> findShortestPathInfo(String upStation, String downStation) {
        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(upStation, downStation).getVertexList();
    }
}
