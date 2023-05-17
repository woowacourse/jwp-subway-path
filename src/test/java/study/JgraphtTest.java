package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JgraphtTest {
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        init(graph);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
        final double pathWeight = dijkstraShortestPath.getPathWeight("v3", "v1");

        Assertions.assertAll(
                () -> assertThat(shortestPath.size()).isEqualTo(3),
                () -> assertThat(shortestPath).containsExactly("v3", "v2", "v1"),
                () -> assertThat(pathWeight).isEqualTo(4)
        );
    }

    @Test
    @DisplayName("그래프 초기화 테스트")
    void removeAll() {
        //given
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        init(graph);

        //when
        final Set<String> strings = new HashSet<>(graph.vertexSet());
        for (String string : strings) {
            graph.removeVertex(string);
        }

        Assertions.assertAll(
                () -> assertThat(graph.vertexSet().size()).isEqualTo(0),
                () -> assertThat(graph.edgeSet().size()).isEqualTo(0)
        );
        //then
    }

    private void init(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);
    }
}
