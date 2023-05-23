package subway.domain.route;

import java.util.List;
import subway.domain.Distance;

public interface RouteFinder {

  List<String> findShortestRoute(final String startStation, final String endStation);

  Distance findShortestRouteDistance(final String startStation, final String endStation);

  List<EdgeSection> findShortestRouteSections(final String startStation, final String endStation);
}
