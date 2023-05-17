package subway.study;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GraphStudyTest {
    @DisplayName("최단 경로를 구할 수 있다.")
    @Test
    void Test() {
        //given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        final DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        //when
        final List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        //then
        assertThat(shortestPath).hasSize(3);
    }

    @DisplayName("WeightedMultigraph 는 양방향 이동이 가능하다.")
    @Test
    void Test2() {
        //given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        final DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        //when
        final List<String> shortestPath
                = dijkstraShortestPath.getPath("v1", "v3").getVertexList();

        //then
        assertThat(shortestPath).hasSize(3);
    }

    @DisplayName("경로 길이를 구할 수 있다.")
    @Test
    void Test3() {
        //given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        final DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        //when
        final double pathWeight = dijkstraShortestPath.getPathWeight("v1", "v2");

        //then
        assertThat(pathWeight).isEqualTo(2);
    }

    @DisplayName("같은 역이면 거리는 0 경로는 자신만 나온다.")
    @Test
    void Test4() {
        //given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        final DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        //when
        final double pathWeight = dijkstraShortestPath.getPathWeight("v1", "v1");
        final List<String> shortestPath
                = dijkstraShortestPath.getPath("v1", "v1").getVertexList();

        //then
        assertAll(
                () -> assertThat(pathWeight).isEqualTo(0),
                () -> assertThat(shortestPath).containsExactly("v1")
        );
    }

}
