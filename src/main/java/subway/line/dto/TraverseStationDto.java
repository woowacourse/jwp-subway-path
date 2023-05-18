package subway.line.dto;

public class TraverseStationDto {

  private final String lineName;
  private final String stationName;

  public TraverseStationDto(String lineName, String stationName) {
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
