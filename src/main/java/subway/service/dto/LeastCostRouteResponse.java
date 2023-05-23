package subway.service.dto;

public class LeastCostRouteResponse {

  private final String startStation;
  private final String endStation;
  private final int cost;
  private final int distance;

  public LeastCostRouteResponse(
      final String startStation,
      final String endStation,
      final int cost,
      final int distance
  ) {
    this.startStation = startStation;
    this.endStation = endStation;
    this.cost = cost;
    this.distance = distance;
  }

  public String getStartStation() {
    return startStation;
  }

  public String getEndStation() {
    return endStation;
  }

  public int getCost() {
    return cost;
  }

  public int getDistance() {
    return distance;
  }
}
