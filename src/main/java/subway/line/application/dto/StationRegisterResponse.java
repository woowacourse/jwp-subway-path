package subway.line.application.dto;

public class StationRegisterResponse {

  private final String lineName;
  private final String stationName;

  public StationRegisterResponse(final String lineName, final String stationName) {
    this.lineName = lineName;
    this.stationName = stationName;
  }

  public String getLineName() {
    return lineName;
  }

  public String getStationName() {
    return stationName;
  }
}
