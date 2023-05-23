package subway.service.dto;

public class SectionResponse {

  private final String currentStationName;
  private final String nextStationName;
  private final int distance;

  public SectionResponse(final String currentStationName, final String nextStationName,
      final int distance) {
    this.currentStationName = currentStationName;
    this.nextStationName = nextStationName;
    this.distance = distance;
  }

  public String getCurrentStationName() {
    return currentStationName;
  }

  public String getNextStationName() {
    return nextStationName;
  }

  public int getDistance() {
    return distance;
  }
}
