package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.ROUTE_NOT_EXISTS;
import static subway.fixture.SectionFixture.잠실_신림_구간_정보;
import static subway.fixture.SectionFixture.잠실_신림_환승_구간_정보;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.복정역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.station.Station;
import subway.exception.BadRequestException;

class RouteGraphTest {

    @Test
    @DisplayName("이동 가능한 모든 경로를 반환한다.")
    void addPossibleRoute() {
        // given
        final RouteGraph routeGraph = new RouteGraph();

        // when
        routeGraph.addPossibleRoute(잠실_신림_구간_정보());

        // then
        final WeightedMultigraph<Station, DefaultWeightedEdge> allRouteGraph = routeGraph.getRouteGraph();
        assertAll(
            () -> assertThat(allRouteGraph.containsVertex(잠실역)).isTrue(),
            () -> assertThat(allRouteGraph.containsVertex(선릉역)).isTrue(),
            () -> assertThat(allRouteGraph.containsVertex(강남역)).isTrue(),
            () -> assertThat(allRouteGraph.containsVertex(복정역)).isTrue(),
            () -> assertThat(allRouteGraph.containsVertex(신림역)).isTrue(),
            () -> assertThat(allRouteGraph.containsEdge(잠실역, 선릉역)).isTrue(),
            () -> assertThat(allRouteGraph.containsEdge(선릉역, 강남역)).isTrue(),
            () -> assertThat(allRouteGraph.containsEdge(강남역, 복정역)).isTrue(),
            () -> assertThat(allRouteGraph.containsEdge(복정역, 신림역)).isTrue()
        );
    }

    @ParameterizedTest(name = "출발역에서 도착역으로 갈 수 있는 경로가 존재하면 아무 일도 일어나지 않는다.")
    @CsvSource(value = {"잠실역:신림역", "신림역:잠실역"}, delimiter = ':')
    void validateRequestRoute_success(final String sourceStationName, final String targetStationName) {
        // given
        final RouteGraph routeGraph = new RouteGraph();
        routeGraph.addPossibleRoute(잠실_신림_구간_정보());
        routeGraph.addPossibleRoute(잠실_신림_환승_구간_정보());

        // expected
        final Station sourceStation = new Station(sourceStationName);
        final Station targetStation = new Station(targetStationName);
        assertDoesNotThrow(() -> routeGraph.validateRequestRoute(sourceStation, targetStation));
    }

    @ParameterizedTest(name = "출발역에서 도착역으로 갈 수 있는 경로가 존재하지 않으면 예외가 발생한다.")
    @CsvSource(value = {"잠실역:바론역", "바론역:신림역"}, delimiter = ':')
    void validateRequestRoute_exception(final String sourceStationName, final String targetStationName) {
        // given
        final RouteGraph routeGraph = new RouteGraph();
        routeGraph.addPossibleRoute(잠실_신림_구간_정보());

        // expected
        final Station sourceStation = new Station(sourceStationName);
        final Station targetStation = new Station(targetStationName);
        assertThatThrownBy(() -> routeGraph.validateRequestRoute(sourceStation, targetStation))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ROUTE_NOT_EXISTS);
    }
}
