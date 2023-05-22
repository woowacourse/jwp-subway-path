package subway.domain.route;

import java.util.List;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Station;

public interface RouteGraph {

    List<Station> findShortestRoute(Station start, Station end);

    Distance findShortestDistance(Station start, Station end);

    List<Line> findLinesOnRoutes(Station start, Station end);
}
