package subway.business.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.FixtureForSubwayMapTest.강남;
import static subway.fixture.FixtureForSubwayMapTest.고속터미널;
import static subway.fixture.FixtureForSubwayMapTest.교대_2호선;
import static subway.fixture.FixtureForSubwayMapTest.교대_2호선_3호선;
import static subway.fixture.FixtureForSubwayMapTest.교대_3호선;
import static subway.fixture.FixtureForSubwayMapTest.낙성대;
import static subway.fixture.FixtureForSubwayMapTest.동작;
import static subway.fixture.FixtureForSubwayMapTest.방배;
import static subway.fixture.FixtureForSubwayMapTest.봉천;
import static subway.fixture.FixtureForSubwayMapTest.사당_2호선;
import static subway.fixture.FixtureForSubwayMapTest.사당_2호선_4호선;
import static subway.fixture.FixtureForSubwayMapTest.사당_4호선;
import static subway.fixture.FixtureForSubwayMapTest.사호선;
import static subway.fixture.FixtureForSubwayMapTest.삼호선;
import static subway.fixture.FixtureForSubwayMapTest.서울대입구;
import static subway.fixture.FixtureForSubwayMapTest.서초;
import static subway.fixture.FixtureForSubwayMapTest.이호선;
import static subway.fixture.FixtureForSubwayMapTest.총신대입구;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.business.domain.line.Station;
import subway.business.domain.line.Stations;

class JgraphtSubwayMapTest {

    private final SubwayMap subwayMap = JgraphtSubwayMap.of(
            List.of(이호선, 삼호선, 사호선),
            List.of(사당_2호선_4호선, 교대_2호선_3호선)
    );

    @DisplayName("최단 거리 경로 조회")
    @Nested
    class calculateShortestPath {

        @DisplayName("단일 노선인 경우, 올바른 경로를 반환한다.")
        @Test
        void shouldFindShortestPathWhenNotTransfer() {
            List<Stations> shortestPath = subwayMap.calculateShortestPath(봉천, 강남);

            Stations stations = shortestPath.get(0);

            assertThat(shortestPath).hasSize(1);
            assertThat(stations.getStations()).containsExactly(봉천, 서울대입구, 낙성대, 사당_2호선, 방배, 서초, 교대_2호선, 강남);
        }

        @DisplayName("환승이 1회 있는 경우, 올바른 경로를 반환한다.")
        @Test
        void shouldFindShortestPathWhenTransfer1Times() {
            List<Stations> shortestPath = subwayMap.calculateShortestPath(낙성대, 고속터미널);

            Stations stations1 = shortestPath.get(0);
            Stations stations2 = shortestPath.get(1);

            assertThat(shortestPath).hasSize(2);
            assertThat(stations1.getStations()).containsExactly(낙성대, 사당_2호선, 방배, 서초, 교대_2호선);
            assertThat(stations2.getStations()).containsExactly(교대_3호선, 고속터미널);
        }

        @DisplayName("환승이 2회 있는 경우, 올바른 경로를 반환한다.")
        @Test
        void shouldFindShortestPathWhenTransfer2Times() {
            List<Stations> shortestPath = subwayMap.calculateShortestPath(고속터미널, 동작);

            Stations stations1 = shortestPath.get(0);
            Stations stations2 = shortestPath.get(1);
            Stations stations3 = shortestPath.get(2);

            assertThat(shortestPath).hasSize(3);
            assertThat(stations1.getStations()).containsExactly(고속터미널, 교대_3호선);
            assertThat(stations2.getStations()).containsExactly(교대_2호선, 서초, 방배, 사당_2호선);
            assertThat(stations3.getStations()).containsExactly(사당_4호선, 총신대입구, 동작);
        }

        @DisplayName("출발 역과 도착 역이 같은 경로를 조회하면 예외가 발생한다.")
        @Test
        void shouldThrowExceptionWhenSourceAndTargetAreSame() {
            assertThatThrownBy(() -> subwayMap.calculateShortestPath(강남, 강남))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("출발 역과 도착 역이 같아 경로를 계산할 수 없습니다. "
                            + "(입력 받은 역 : 강남)");
        }
    }

    @DisplayName("경로에 알맞은 거리를 반환한다.")
    @ParameterizedTest
    @MethodSource("stationData")
    void shouldReturnAppropriateFareWhenRequest(Station sourceStation, Station targetStation, int expectedFare) {
        int distance = subwayMap.calculateDistanceOfPath(sourceStation, targetStation);
        assertThat(distance).isEqualTo(expectedFare);
    }

    static Stream<Arguments> stationData() {
        return Stream.of(
                Arguments.of(봉천, 서울대입구, 5),
                Arguments.of(봉천, 사당_2호선, 15),
                Arguments.of(봉천, 동작, 25),
                Arguments.of(동작, 고속터미널, 30)
        );
    }
}
