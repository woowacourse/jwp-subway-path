package subway.line.dto;

import java.util.List;

public class ShortestPathResponse {

  private final double distance;
  private final int fare;
  private final List<TraverseStationDto> traverseStationDtos;

  public ShortestPathResponse(double distance, int fare, List<TraverseStationDto> traverseStationDtos) {
    this.distance = distance;
    this.fare = fare;
    this.traverseStationDtos = traverseStationDtos;
  }

  public double getDistance() {
    return distance;
  }

  public int getFare() {
    return fare;
  }

  public List<TraverseStationDto> getTraverseStationDtos() {
    return traverseStationDtos;
  }
}
