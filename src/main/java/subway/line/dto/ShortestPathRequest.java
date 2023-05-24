package subway.line.dto;

public class ShortestPathRequest {

  private final Long fromStationId;
  private final Long toStationId;

  public ShortestPathRequest(Long fromStationId, Long toStationId) {
    this.fromStationId = fromStationId;
    this.toStationId = toStationId;
  }

  public Long getFromStationId() {
    return fromStationId;
  }

  public Long getToStationId() {
    return toStationId;
  }
}
