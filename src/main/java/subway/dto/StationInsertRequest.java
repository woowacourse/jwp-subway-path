package subway.dto;

public class StationInsertRequest {

    private final Long stationId;
    private final Long lineId;
    private final Long adjacentStationId;
    private final String direction;
    private final Integer distance;

    public StationInsertRequest(
            final Long stationId,
            final Long lineId,
            final Long adjacentStationId,
            final String direction,
            final Integer distance
    ) {
        this.stationId = stationId;
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
