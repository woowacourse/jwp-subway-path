package subway.study;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JgraphtTest {

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

    @Test
    void 객체로_구성된_최단경로를_찾을_수_있다() {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        Station 선릉역 = new Station("선릉역");

        graph.addVertex(강남역);
        graph.addVertex(서초역);
        graph.addVertex(선릉역);

        graph.addEdge(강남역, 서초역, new Section(강남역, 서초역, 2));
        graph.addEdge(서초역, 선릉역, new Section(서초역, 선릉역, 3));

        graph.setEdgeWeight(강남역, 서초역, 2);
        graph.setEdgeWeight(서초역, 선릉역, 3);

        DijkstraShortestPath<Station, Section> dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> vertexList = dijkstraShortestPath.getPath(강남역, 선릉역).getVertexList();

        assertThat(vertexList)
                .isEqualTo(List.of(강남역, 서초역, 선릉역));
    }
}
