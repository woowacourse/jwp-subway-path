package subway.domain.route;

import subway.domain.Station;

public interface RouteMap {
    Path findShortestPath(final Station startStation, final Station endStation);
}
