package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.남영역;
import static subway.fixture.StationFixture.사당역;
import static subway.fixture.StationFixture.서울역;
import static subway.fixture.StationFixture.서초역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.시청역;
import static subway.fixture.StationFixture.신사역;
import static subway.fixture.StationFixture.역삼역;
import static subway.fixture.StationFixture.용산역;
import static subway.fixture.StationFixture.종각역;
import static subway.fixture.StationFixture.회현역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.domain.route.JgraphtRouteGraph;
import subway.fixture.LineFixture;

@SuppressWarnings("NonAsciiCharacters")
class JgraphtRouteGraphTest {

    @Test
    @DisplayName("출발역과 도착역 까지 최단 경로를 찾는다.")
    void findShortedRoute1() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        List<Station> shortestRoute = jgraphtRouteGraph.findShortestRoute(남영역, 선릉역);

        assertThat(shortestRoute).containsExactly(남영역, 서울역, 시청역, 선릉역);
    }

    @Test
    @DisplayName("출발역과 도착역 까지 환승하여 최단 경로를 찾는다.")
    void findShortedRouteWithTransfer() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        List<Station> shortestRoute = jgraphtRouteGraph.findShortestRoute(사당역, 종각역);

        assertThat(shortestRoute).containsExactly(사당역, 용산역, 시청역, 종각역);
    }

    @Test
    @DisplayName("출발역과 도착역 까지 최단 거리를 찾는다.")
    void findShortedDistance() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        Distance distance = jgraphtRouteGraph.findShortestDistance(남영역, 선릉역);

        assertThat(distance.getValue()).isEqualTo(20);
    }

    @Test
    @DisplayName("출발역과 도착역 까지 환승하여 최단 거리를 찾는다.")
    void findShortedDistanceWithTransfer() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        Distance distance = jgraphtRouteGraph.findShortestDistance(강남역, 선릉역);

        assertThat(distance.getValue()).isEqualTo(20);
    }

    @Test
    @DisplayName("출발역이 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongStart() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        assertThatThrownBy(() -> jgraphtRouteGraph.findShortestRoute(역삼역, 선릉역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("도착역이 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongEnd() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        assertThatThrownBy(() -> jgraphtRouteGraph.findShortestRoute(선릉역, 회현역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역 모두 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongStations() {
        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        assertThatThrownBy(() -> jgraphtRouteGraph.findShortestRoute(서초역, 회현역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역의 경로가 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteWithNoResult() {
        Station start = 신사역;
        Station end = 서울역;

        JgraphtRouteGraph jgraphtRouteGraph = createRouteMap();

        assertThatThrownBy(() -> jgraphtRouteGraph.findShortestRoute(신사역, 서울역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(start.getName() + "과 " + end.getName() + " 사이의 경로가 존재하지 않습니다.");
    }
    
    private static JgraphtRouteGraph createRouteMap() {
        Line 일호선 = LineFixture.일호선;
        Line 이호선 = LineFixture.이호선;
        Line 삼호선 = LineFixture.삼호선;
        Line 사호선 = LineFixture.사호선;
        return JgraphtRouteGraph.from(List.of(일호선, 이호선, 삼호선, 사호선));
    }
}
