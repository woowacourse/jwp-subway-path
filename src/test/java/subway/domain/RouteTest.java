package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import subway.fixture.LineFixture;

@SuppressWarnings("NonAsciiCharacters")
class RouteTest {

    @Test
    @DisplayName("출발역과 도착역 까지 최단 경로를 찾는다.")
    void findShortedRoute1() {
        Station start = 남영역;
        Station end = 선릉역;
        Route route = createRouteMap();

        List<Station> shortestRoute = route.findShortestRoute(start, end);

        assertThat(shortestRoute).containsExactly(start, 서울역, 시청역, end);
    }

    @Test
    @DisplayName("출발역과 도착역 까지 최단 경로를 찾는다.")
    void findShortedRoute2() {
        Station start = 사당역;
        Station end = 종각역;
        Route route = createRouteMap();

        List<Station> shortestRoute = route.findShortestRoute(start, end);

        assertThat(shortestRoute).containsExactly(start, 용산역, 시청역, end);
    }

    @Test
    @DisplayName("출발역과 도착역 까지 최단 거리를 찾는다.")
    void findShortedDistance() {
        Station start = 남영역;
        Station end = 선릉역;
        Route route = createRouteMap();

        Distance distance = route.findShortestDistance(start, end);

        assertThat(distance.getValue()).isEqualTo(20);
    }

    @Test
    @DisplayName("출발역이 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongStart() {
        Station start = 역삼역;
        Station end = 선릉역;

        Route route = createRouteMap();

        assertThatThrownBy(() -> route.findShortestRoute(start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("도착역이 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongEnd() {
        Station start = 선릉역;
        Station end = 회현역;

        Route route = createRouteMap();

        assertThatThrownBy(() -> route.findShortestRoute(start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역 모두 전체 경로의 역에 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteFailWithWrongStations() {
        Station start = 서초역;
        Station end = 회현역;

        Route route = createRouteMap();

        assertThatThrownBy(() -> route.findShortestRoute(start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역의 경로가 존재하지 않는 경우 예외가 발생한다.")
    void findShortestRouteWithNoResult() {
        Station start = 신사역;
        Station end = 서울역;

        Route route = createRouteMap();

        assertThatThrownBy(() -> route.findShortestRoute(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(start.getName() + "과 " + end.getName() + " 사이의 경로가 존재하지 않습니다.");
    }


    private static Route createRouteMap() {
        Line 일호선 = LineFixture.일호선;
        Line 이호선 = LineFixture.이호선;
        Line 삼호선 = LineFixture.삼호선;
        Line 사호선 = LineFixture.사호선;
        return Route.from(List.of(일호선, 이호선, 삼호선, 사호선));
    }
}
