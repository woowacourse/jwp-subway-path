package subway.route.domain.jgraph;

import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.route.domain.RouteSegment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.domain.SectionFixture.DISTANCE;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;

class JgraphRouteFinderBuilderTest {

    @Test
    void buildRouteFinder() {
        final JgraphRouteFinderBuilder routeFinderBuilder = new JgraphRouteFinderBuilder();
        final List<Line> lineNumberTwo = List.of(LINE_NUMBER_TWO);

        final JgraphRouteFinder routeFinder = (JgraphRouteFinder) routeFinderBuilder.buildRouteFinder(lineNumberTwo);

        final RouteSegment route = routeFinder.getRoute(JAMSIL_STATION, JAMSIL_NARU_STATION)
                                              .get(0);

        assertAll(
                () -> assertThat(route.getUpstreamId()).isEqualTo(JAMSIL_STATION.getId()),
                () -> assertThat(route.getDownstreamId()).isEqualTo(JAMSIL_NARU_STATION.getId()),
                () -> assertThat(route.getUpstreamName()).isEqualTo(JAMSIL_STATION.getName()),
                () -> assertThat(route.getDownstreamName()).isEqualTo(JAMSIL_NARU_STATION.getName()),
                () -> assertThat(route.getLineId()).isEqualTo(LINE_NUMBER_TWO.getId()),
                () -> assertThat(route.getLineName()).isEqualTo(LINE_NUMBER_TWO.getName()),
                () -> assertThat(route.getDistance()).isEqualTo(DISTANCE)
        );
    }
}
