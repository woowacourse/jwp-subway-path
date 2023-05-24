package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.LineFixture.FIXTURE_LINE_1;
import static subway.fixture.LineFixture.FIXTURE_LINE_2;
import static subway.fixture.LineFixture.FIXTURE_LINE_3;
import static subway.fixture.SectionFixture.ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1;
import static subway.fixture.SectionFixture.ROUTED_SECTIONS_2_TRANSFER_1_AT_ST1_3_AT_ST9;
import static subway.fixture.SectionFixture.ROUTED_SECTIONS_3_TRANSFER_1_AT_ST2_TRANSFER_2_AT_ST9;
import static subway.fixture.SectionFixture.SECTION_ST1_ST2;
import static subway.fixture.SectionFixture.SECTION_ST2_ST9;
import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_2;
import static subway.fixture.StationFixture.FIXTURE_STATION_7;
import static subway.fixture.StationFixture.FIXTURE_STATION_9;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.entity.Line;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.IllegalSubwayMapArgumentException;

@DisplayName("지하철 노선도 단위 테스트")
class SubwayMapTest {

    @DisplayName("출발 역과 도착 역을 전달받아 환승 포함 최단 거리 경로 정보를 반환한다")
    @Test
    void findShortestRoutedStations() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(
                FIXTURE_LINE_1, RoutedStations.from(ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1),
                FIXTURE_LINE_2, RoutedStations.from(ROUTED_SECTIONS_2_TRANSFER_1_AT_ST1_3_AT_ST9),
                FIXTURE_LINE_3, RoutedStations.from(ROUTED_SECTIONS_3_TRANSFER_1_AT_ST2_TRANSFER_2_AT_ST9)
        );
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when
        TransferableRoute shortestRoute = subwayMap.findShortestRoute(FIXTURE_STATION_1, FIXTURE_STATION_9);

        // then
        assertThat(shortestRoute.stations())
                .contains(FIXTURE_STATION_1, FIXTURE_STATION_2, FIXTURE_STATION_9);
        assertThat(shortestRoute.totalDistance())
                .isEqualTo(SECTION_ST1_ST2.getDistance().plus(SECTION_ST2_ST9.getDistance()));
    }

    @DisplayName("출발 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRoutedStationsFailNotExistingSource() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(FIXTURE_LINE_1, RoutedStations.from(
                ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1));
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when, then
        assertThatThrownBy(
                () -> subwayMap.findShortestRoute(FIXTURE_STATION_7, FIXTURE_STATION_1))
                .isInstanceOf(EmptyRoutedStationsSearchResultException.class)
                .hasMessageContaining("지하철 노선도에 출발 역이 존재하지 않습니다.");
    }

    @DisplayName("도착 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRoutedStationsFailNotExistingTarget() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(FIXTURE_LINE_1, RoutedStations.from(
                ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1));
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when, then
        assertThatThrownBy(
                () -> subwayMap.findShortestRoute(FIXTURE_STATION_1, FIXTURE_STATION_7))
                .isInstanceOf(EmptyRoutedStationsSearchResultException.class)
                .hasMessageContaining("지하철 노선도에 도착 역이 존재하지 않습니다.");
    }

    @DisplayName("출발 역과 도착 역이 동일하면 예외를 발생한다")
    @Test
    void findShortestRoutedStationsFailSourceTargetSame() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(FIXTURE_LINE_1, RoutedStations.from(
                ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1));
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when, then
        assertThatThrownBy(
                () -> subwayMap.findShortestRoute(FIXTURE_STATION_1, FIXTURE_STATION_1))
                .isInstanceOf(IllegalSubwayMapArgumentException.class)
                .hasMessageContaining("출발 역과 도착 역이 동일한 경로를 찾을 수 없습니다.");
    }
}
