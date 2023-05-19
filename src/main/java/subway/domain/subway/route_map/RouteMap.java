package subway.domain.subway.route_map;

import subway.domain.Path;
import subway.domain.station.Station;

public interface RouteMap {

    Path findShortestPath(Station source, Station destination);
}
