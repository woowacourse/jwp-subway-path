package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JgraphtTest {
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
        final double pathWeight = dijkstraShortestPath.getPathWeight("v3", "v1");

        Assertions.assertAll(
                () -> assertThat(shortestPath.size()).isEqualTo(3),
                () -> assertThat(shortestPath).containsExactly("v3", "v2", "v1"),
                () -> assertThat(pathWeight).isEqualTo(4)
        );
    }
}
