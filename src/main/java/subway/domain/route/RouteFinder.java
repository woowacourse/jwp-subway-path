package subway.domain.route;

import subway.domain.Distance;

import java.util.List;

public interface RouteFinder {

    List<String> findShortestRoute(final String startStation, final String endStation);

    Distance findShortestRouteDistance(final String startStation, final String endStation);

    List<EdgeSection> findShortestRouteSections(final String startStation, final String endStation);
}
