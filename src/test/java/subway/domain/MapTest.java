package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.TestFeature.*;

class MapTest {

    @DisplayName("시작, 끝 역을 넣으면 그에 따른 최단 거리 경로를 반환")
    @Test
    void getShortestPath() {
        // given
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stationGraph.addVertex(STATION_서울대입구);
        stationGraph.addVertex(STATION_봉천역);
        stationGraph.addVertex(STATION_낙성대역);
        stationGraph.addVertex(STATION_사당역);
        stationGraph.addVertex(STATION_방배역);
        stationGraph.setEdgeWeight(stationGraph.addEdge(STATION_서울대입구, STATION_봉천역), 6);
        stationGraph.setEdgeWeight(stationGraph.addEdge(STATION_봉천역, STATION_낙성대역), 9);
        stationGraph.setEdgeWeight(stationGraph.addEdge(STATION_낙성대역, STATION_사당역), 8);
        stationGraph.setEdgeWeight(stationGraph.addEdge(STATION_방배역, STATION_낙성대역), 12);
        Map map = new Map(stationGraph);

        // when
        Path shortestPath = map.getShortestPath(STATION_서울대입구, STATION_방배역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getPath()).isEqualTo(List.of("서울대입구역", "봉천역", "낙성대역", "방배역")),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(27)
        );
    }
}
