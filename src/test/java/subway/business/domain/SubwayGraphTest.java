package subway.business.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixtures.station.StationFixture.*;

public class SubwayGraphTest {

    @DisplayName("역과 역 사이의 최단 경로 루트(역 리스트)를 반환한다.")
    @Test
    void shouldReturnShortestPathWhenRequest() {
        // line1 : (상행) 강남역 -10km- 역삼역 -10km- 잠실역 -10km- 성수역 (하행)
        // line2 : (상행) 역삼역 -15km- 성수역(하행)
        Line line1 = Line.of("2호선", 강남역, 역삼역, 10);
        line1.addStation(잠실역, 역삼역, Direction.DOWNWARD, 10);
        line1.addStation(성수역, 잠실역, Direction.DOWNWARD, 10);
        Line line2 = Line.of("3호선", 역삼역, 성수역, 15);
        Subway subway = new Subway(List.of(line1, line2));
        SubwayGraph subwayGraph = SubwayGraph.from(subway);

        List<Station> path = subwayGraph.getShortestPath(강남역, 성수역);

        assertThat(path).isEqualTo(List.of(강남역, 역삼역, 성수역));
    }

    @DisplayName("역과 역 사이의 최단 경로 거리를 반환한다.")
    @Test
    void shouldReturnDistanceOfShortestPathWhenRequest() {
        // line1 : (상행) 강남역 -10km- 역삼역 -10km- 잠실역 -10km- 성수역 (하행)
        // line2 : (상행) 역삼역 -15km- 성수역(하행)
        Line line1 = Line.of("2호선", 강남역, 역삼역, 10);
        line1.addStation(잠실역, 역삼역, Direction.DOWNWARD, 10);
        line1.addStation(성수역, 잠실역, Direction.DOWNWARD, 10);
        Line line2 = Line.of("3호선", 역삼역, 성수역, 15);
        Subway subway = new Subway(List.of(line1, line2));
        SubwayGraph subwayGraph = SubwayGraph.from(subway);

        int totalDistance = subwayGraph.getTotalDistance(강남역, 성수역);

        assertThat(totalDistance).isEqualTo(25);
    }
}
