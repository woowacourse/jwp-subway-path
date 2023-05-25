package subway.domain.route;

import java.util.List;
import subway.domain.Distance;
import subway.domain.station.Station;

public interface RouteFinder {

  List<Station> findShortestRoute(final Station departure, final Station arrival);

  Distance findShortestRouteDistance(final Station departure, final Station arrival);

  List<EdgeSection> findShortestRouteSections(final Station departure, final Station arrival);
}
