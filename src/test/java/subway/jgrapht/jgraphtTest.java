package subway.jgrapht;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.integration.IntegrationFixture.*;

public class jgraphtTest {

    @DisplayName("최단 경로 검색을 테스트한다.")
    @Test
    void dijkstraShortestPathTest() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(STATION_A);
        graph.addVertex(STATION_B);
        graph.addVertex(STATION_C);
        graph.setEdgeWeight(graph.addEdge(STATION_A, STATION_B), 2);
        graph.setEdgeWeight(graph.addEdge(STATION_B, STATION_C), 2);
        graph.setEdgeWeight(graph.addEdge(STATION_A, STATION_C), 100);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        final List<Station> shortestPath = dijkstraShortestPath.getPath(STATION_C, STATION_A)
                .getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }
}
