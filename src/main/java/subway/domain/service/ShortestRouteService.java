package subway.domain.service;

import java.util.List;
import subway.domain.Line;
import subway.domain.LinkedRoute;
import subway.domain.Station;

public interface ShortestRouteService {

    LinkedRoute shortestRoute(final List<Line> lines, final Station start, final Station end);
}
