package subway.domain.section;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class WeightedMultiGraphTest {

    @Test
    void 그래프_자료_구조_라이브러리_학습_테스트() {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.addVertex("d");
        graph.setEdgeWeight(graph.addEdge("a", "b"), 3);
        graph.setEdgeWeight(graph.addEdge("d", "c"), 1);
        graph.setEdgeWeight(graph.addEdge("b", "c"), 4);

        final var dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final List<String> vertexList = dijkstraShortestPath.getPath("a", "d").getVertexList();

        System.out.println("vertexList = " + vertexList);
    }
}
