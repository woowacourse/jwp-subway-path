package subway.domain;

import org.assertj.core.api.Assertions;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayStructureTest {

    @DisplayName("첫 시작점을 찾을 수 있다.")
    @Test
    void findStartStation() {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        Station station1 = StationFixture.FIXTURE_STATION_1;
        Station station2 = StationFixture.FIXTURE_STATION_2;
        Station station3 = StationFixture.FIXTURE_STATION_3;
        graph.addVertex(station1);
        graph.addVertex(station2);
        graph.addVertex(station3);
        graph.setEdgeWeight(graph.addEdge(station1, station2), 10);
        graph.setEdgeWeight(graph.addEdge(station2, station3), 10);

        SubwayStructure subwayStructure = new SubwayStructure(graph);

        assertThat(subwayStructure.findStartStation()).isEqualTo(station1);
    }
}
