package subway.utils;

import subway.line.domain.Line;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteSegment;
import subway.route.domain.jgraph.JgraphRouteFinderBuilder;

import java.util.List;

import static subway.utils.LineFixture.LINE_NUMBER_FOUR;
import static subway.utils.LineFixture.LINE_NUMBER_THREE;

public class RouteFinderFixture {

    /**
     * 3호선: 잠실 --10-- 잠실나루 -------97------- 선릉
     * 4호선:            잠실나루 --40-- 강남 --24-- 선릉
     *
     */

    public static RouteFinder<RouteSegment> getRouteFinder() {
        final List<Line> lines = List.of(LINE_NUMBER_THREE, LINE_NUMBER_FOUR);
        final JgraphRouteFinderBuilder routeFinderBuilder = new JgraphRouteFinderBuilder();
        return routeFinderBuilder.buildRouteFinder(lines);
    }
}
