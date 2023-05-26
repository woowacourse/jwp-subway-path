package subway.route.application;

import java.util.List;
import subway.value_object.Distance;
import subway.line.domain.Line;
import subway.line.domain.Station;
import subway.route.infrastructure.DijkstraGraphFactory;
import subway.route.infrastructure.EdgeSection;

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
