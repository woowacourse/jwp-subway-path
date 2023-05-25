package subway.domain.route;

import java.util.List;
import subway.domain.Distance;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class JgraphtRouteFinder {

  private JgraphtRouteFinder() {
  }

  public static List<Station> findShortestRoute(
      final List<Line> lines,
      final Station departure,
      final Station arrival
  ) {
    return DijkstraGraphFactory.makeDijkstraGraph(lines).getPath(departure, arrival)
        .getVertexList();
  }

  public static Distance findShortestRouteDistance(
      final List<Line> lines,
      final Station departure,
      final Station arrival
  ) {
    return new Distance(
        (int) DijkstraGraphFactory.makeDijkstraGraph(lines).getPathWeight(departure, arrival));
  }

  public static List<EdgeSection> findShortestRouteSections(
      final List<Line> lines,
      final Station departure,
      final Station arrival
  ) {
    return DijkstraGraphFactory.makeDijkstraGraph(lines).getPath(departure, arrival).getEdgeList();
  }
}
