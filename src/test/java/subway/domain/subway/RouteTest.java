package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.LinesEmptyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LinesFixture.createLines;

class RouteTest {

    @Test
    @DisplayName("최단 경로를 찾아준다.")
    void find_shortest_route_with_lines_name() {
        // given
        int defaultFee = 1250;
        Route route = Route.from(createLines());

        // when
        List<Station> result = route.findShortestPath("잠실역", "종합운동장역");

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.contains(new Station("잠실역"))).isTrue(),
                () -> assertThat(route.getFee()).isEqualTo(defaultFee)
        );
    }

    @Test
    @DisplayName("최단 경로의 환승역을 찾아준다.")
    void find_shortest_transfer_lines() {
        // given
        Route route = Route.from(createLines());

        // when
        List<Set<String>> result = route.findShortestTransferLines("잠실역", "종합운동장역");

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(3),
                () -> assertThat(result.get(0).contains("2호선")).isTrue()
        );
    }

    @DisplayName("최단 경로를 찾을 때, Lines가 null이면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_find_shortest_route_but_lines_null() {
        // given
        Route routeOfNull = Route.createDefault();

        // when & then
        assertThatThrownBy(() -> routeOfNull.findShortestPath("선릉역", "잠실역"))
                .isInstanceOf(LinesEmptyException.class);
    }

    @DisplayName("최단 경로를 찾을 때, Lines의 사이즈가 0이라면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_find_shortest_route_but_lines_empty() {
        // given
        Route routeOfEmpty = Route.from(new Lines(new ArrayList<>()));

        // when & then
        assertThatThrownBy(() -> routeOfEmpty.findShortestPath("선릉역", "잠실역"))
                .isInstanceOf(LinesEmptyException.class);
    }
}
