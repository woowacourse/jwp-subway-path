package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.SectionFixture.잠실_신림_구간_정보;
import static subway.fixture.SectionFixture.잠실_신림_환승_구간_정보;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;

class ShortestRouteTest {

    @Test
    @DisplayName("모든 구간 정보 중에서 도착지로 갈 수 있는 경로 중 가장 짧은 거리를 구한다.")
    void getShortestDistance() {
        // given
        final RouteGraph routeGraph = new RouteGraph();
        final Sections 잠실_신림_구간 = 잠실_신림_구간_정보();
        final Sections 잠실_신림_환승_구간 = 잠실_신림_환승_구간_정보();
        routeGraph.addPossibleRoute(잠실_신림_구간);
        routeGraph.addPossibleRoute(잠실_신림_환승_구간);

        final List<Section> 전체_구간 = new ArrayList<>(잠실_신림_구간.getSections());
        전체_구간.addAll(잠실_신림_환승_구간.getSections());
        final ShortestRoute shortestRoute = new ShortestRoute(routeGraph, 잠실역, 신림역);

        // when
        final Distance shortestDistance = shortestRoute.getShortestDistance(전체_구간);

        // then
        assertThat(shortestDistance)
            .isEqualTo(new Distance(20));
    }
}
