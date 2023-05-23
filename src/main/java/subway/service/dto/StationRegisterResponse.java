package subway.service.dto;

public class StationRegisterResponse {

  private String lineName;
  private String stationName;

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
