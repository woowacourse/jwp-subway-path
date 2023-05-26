package subway.line.application.dto;

public class RegisterLineRequest {

  private String currentStationName;
  private String nextStationName;
  private String lineName;
  private int distance;

  private RegisterLineRequest() {
  }

  public RegisterLineRequest(
      final String currentStationName,
      final String nextStationName,
      final String lineName,
      final int distance
  ) {
    this.currentStationName = currentStationName;
    this.nextStationName = nextStationName;
    this.lineName = lineName;
    this.distance = distance;
  }

  public String getCurrentStationName() {
    return currentStationName;
  }

  public String getNextStationName() {
    return nextStationName;
  }

  public String getLineName() {
    return lineName;
  }

  public int getDistance() {
    return distance;
  }
}
