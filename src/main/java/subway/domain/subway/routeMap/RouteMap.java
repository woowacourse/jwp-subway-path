package subway.domain.subway.routeMap;

import subway.domain.Path;
import subway.domain.station.Station;

public interface RouteMap {

    Path findShortestPath(Station source, Station destination);
}
