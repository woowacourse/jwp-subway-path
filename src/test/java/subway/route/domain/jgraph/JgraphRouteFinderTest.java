package subway.route.domain.jgraph;

import org.junit.jupiter.api.Test;
import subway.line.domain.MiddleSection;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.utils.LineFixture.LINE_NUMBER_FOUR;
import static subway.utils.LineFixture.LINE_NUMBER_THREE;
import static subway.utils.RouteFinderFixture.getRouteFinder;
import static subway.utils.StationFixture.*;

class JgraphRouteFinderTest {

    /**
     * 3호선: 잠실 --10-- 잠실나루 -------97------- 선릉
     * 4호선:            잠실나루 --40-- 강남 --24-- 선릉
     *
     */

    @Test
    void getRoute() {
        // given
        List<Integer> expectedDistances = new ArrayList<>();
        final List<MiddleSection> lineNumberThreeSections = LINE_NUMBER_THREE.getSections();
        final List<MiddleSection> lineNumberFourSections = LINE_NUMBER_FOUR.getSections();
        expectedDistances.add(lineNumberThreeSections.get(0).getDistance());
        expectedDistances.add(lineNumberFourSections.get(0).getDistance());
        expectedDistances.add(lineNumberFourSections.get(1).getDistance());

        final RouteFinder<RouteSegment> routeFinder = getRouteFinder();

        // when
        final List<RouteSegment> route = routeFinder.getRoute(JAMSIL_STATION, SULLEUNG_STATION);

        // then
        final List<Integer> actualDistances = route.stream()
                                           .map(RouteSegment::getDistance)
                                           .collect(Collectors.toList());
        assertThat(actualDistances).isEqualTo(expectedDistances);
    }

    @Test
    void getTotalWeight() {
        final RouteFinder<RouteSegment> routeFinder = getRouteFinder();

        assertAll(
                () -> assertThat(routeFinder.getTotalWeight(SULLEUNG_STATION, JAMSIL_STATION)).isEqualTo(74),
                () -> assertThat(routeFinder.getTotalWeight(JAMSIL_NARU_STATION, SULLEUNG_STATION)).isEqualTo(64)
        );
    }
}
