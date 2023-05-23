package subway.domain.route;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

public interface RouteFinder {
    Route findRouteBy(final List<Line> lines, final Station start, final Station end);
}
