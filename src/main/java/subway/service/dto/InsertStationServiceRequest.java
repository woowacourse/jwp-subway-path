package subway.service.dto;

public class InsertStationServiceRequest {
    private final Long stationId;
    private final Long lineId;
    private final Long adjacentStationId;
    private final String direction;
    private final Integer distance;

    public InsertStationServiceRequest(Long AddStationId, Long lineId, Long adjacentStationId, String direction,
                                Integer distance) {
        this.stationId = AddStationId;
        this.lineId = lineId;
        this.adjacentStationId = adjacentStationId;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getAdjacentStationId() {
        return adjacentStationId;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
