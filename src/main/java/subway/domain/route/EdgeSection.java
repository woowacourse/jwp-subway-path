package subway.domain.route;

import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeSection extends DefaultWeightedEdge {

  private final String startStation;
  private final String endStation;
  private final int distance;
  private final String lineName;

  public EdgeSection(
      final String startStation,
      final String endStation,
      final int distance,
      final String lineName
  ) {
    this.startStation = startStation;
    this.endStation = endStation;
    this.distance = distance;
    this.lineName = lineName;
  }

  public String getStartStation() {
    return startStation;
  }

  public String getEndStation() {
    return endStation;
  }

  public String getLineName() {
    return lineName;
  }

  public int getDistance() {
    return distance;
  }
}
