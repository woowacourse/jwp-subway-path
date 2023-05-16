package subway.domain.subway;

import subway.domain.Path;
import subway.domain.station.Station;

public interface SubwayRouteMap {

    Path findShortestPath(Station source, Station destination);
}
