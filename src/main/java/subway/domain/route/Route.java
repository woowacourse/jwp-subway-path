package subway.domain.route;

import java.util.List;
import subway.domain.Distance;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class Route {

  private final RouteFinder routeFinder;
  private final Station from;
  private final Station to;

  public Route(
      final List<Line> lines,
      final Station from,
      final Station to
  ) {
    this.routeFinder = new JgraphtRouteFinder(lines);
    this.from = from;
    this.to = to;
  }

  public List<String> findShortestRoute() {
    return routeFinder.findShortestRoute(from.getName(), to.getName());
  }

  public Distance findShortestRouteDistance() {
    return routeFinder.findShortestRouteDistance(from.getName(), to.getName());
  }

  public List<EdgeSection> findShortestRouteSections() {
    return routeFinder.findShortestRouteSections(from.getName(), to.getName());
  }
}
