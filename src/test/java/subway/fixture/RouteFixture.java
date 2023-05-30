package subway.fixture;

import java.util.List;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.route.Route;
import subway.domain.route.RouteSection;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;

public class RouteFixture {

    public static class 역삼_삼성_10 {

        private static final Section SECTION = new Section(1L, 역삼역.STATION, 삼성역.STATION, 10);
        private static final Line LINE = new Line(1L, "이호선", "GREEN", 100, List.of(SECTION));

        public static final Route ROUTE = new Route(List.of(new RouteSection(LINE, SECTION)));
    }

    public static Route getRouteDistanceOf(final int distance) {
        final Section SECTION = new Section(1L, 역삼역.STATION, 삼성역.STATION, distance);
        final Line LINE = new Line(1L, "이호선", "GREEN", 100, List.of(SECTION));

        return new Route(List.of(new RouteSection(LINE, SECTION)));
    }
}
