package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LinesFixture.createLines;

class RouteTest {

    @Test
    @DisplayName("최단 경로와, 역에 해당하는 노선의 이름을 포함하는 Map을 반환해준다.")
    void find_shortest_route_with_lines_name() {
        // given
        Route route = Route.from(createLines());

        // when
        Map<Station, Set<String>> result = route.findShortestRouteWithLineNames("잠실역", "종합운동장역");
        Set<Station> stations = result.keySet();

        // then
        assertAll(
                () -> assertThat(result.keySet().size()).isEqualTo(3),
                () -> assertThat(stations.contains(new Station("잠실역"))).isTrue()
        );

    }
}
