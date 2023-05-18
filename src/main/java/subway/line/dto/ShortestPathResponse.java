package subway.line.dto;

import java.util.List;

public class ShortestPathResponse {

  private final double distance;
  private final List<TraverseStationDto> traverseStationDtos;

  public ShortestPathResponse(double distance, List<TraverseStationDto> traverseStationDtos) {
    this.distance = distance;
    this.traverseStationDtos = traverseStationDtos;
  }

  public double getDistance() {
    return distance;
  }

  public List<TraverseStationDto> getTraverseStationDtos() {
    return traverseStationDtos;
  }
}
