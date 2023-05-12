package subway.domain.service;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public interface ShortestRouteService {

    List<Line> shortestRoute(final List<Line> lines, final Station start, final Station end);
}
