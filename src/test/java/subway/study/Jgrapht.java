package subway.study;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Jgrapht {

    @Test
    void 최단경로_조회_테스트() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    void 노선_최단경로_조회_테스트() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph.addVertex("강남역");
        graph.addVertex("서초역");
        graph.addVertex("선릉역");
        graph.addVertex("봉천역");
        graph.addVertex("당곡역");
        graph.addVertex("야당역");
        graph.setEdgeWeight(graph.addEdge("강남역", "서초역"), 2);
        graph.setEdgeWeight(graph.addEdge("서초역", "선릉역"), 3);
        graph.setEdgeWeight(graph.addEdge("선릉역", "봉천역"), 5);
        graph.setEdgeWeight(graph.addEdge("봉천역", "당곡역"), 6);


        graph.setEdgeWeight(graph.addEdge("야당역", "선릉역"), 20);
        graph.setEdgeWeight(graph.addEdge("선릉역", "당곡역"), 3);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("강남역", "당곡역").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(4);
    }
}
