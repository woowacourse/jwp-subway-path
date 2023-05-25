package subway.route.application.dto;

public class ShortestRouteRequest {

  private final String startStation;
  private final String endStation;
  private final Integer age;

  public ShortestRouteRequest(final String startStation, final String endStation,
      final Integer age) {
    this.startStation = startStation;
    this.endStation = endStation;
    this.age = age;
  }

  public String getStartStation() {
    return startStation;
  }

  public String getEndStation() {
    return endStation;
  }

  public Integer getAge() {
    return age;
  }
}
