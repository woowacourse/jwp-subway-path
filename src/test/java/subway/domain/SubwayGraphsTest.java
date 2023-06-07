package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SubwayGraphsTest {

    @Test
    @DisplayName("최단 경로를 구할 수 있다.")
    void ss() {
        SubwayGraph subwayGraph1 = new SubwayGraph(new Line("2호선"));
        subwayGraph1.addStation(new Station("A역"), new Station("D역"), 3);
        subwayGraph1.addStation(new Station("D역"), new Station("B역"), 4);
        subwayGraph1.addStation(new Station("B역"), new Station("C역"), 3);
        SubwayGraph subwayGraph2 = new SubwayGraph(new Line("3호선"));
        subwayGraph2.addStation(new Station("E역"), new Station("D역"), 2);
        subwayGraph2.addStation(new Station("D역"), new Station("F역"), 2);
        subwayGraph2.addStation(new Station("F역"), new Station("H역"), 2);
        subwayGraph2.addStation(new Station("H역"), new Station("C역"), 2);
        SubwayGraphs subwayGraphs = new SubwayGraphs(List.of(subwayGraph1, subwayGraph2));

        ShortestPath shortestPath = subwayGraphs.getShortestPath(new Station("B역"), new Station("H역"));

        Assertions.assertThat(shortestPath.getPath()).containsExactly
                (new Station("B역"), new Station("C역"), new Station("H역"));
        Assertions.assertThat(shortestPath.getDistance()).isEqualTo(5);
    }

}