package subway.dto.service;

public class InsertStationServiceCommand {
    private final Long stationId;
    private final Long lineId;
    private final Long adjacentStationId;
    private final String direction;
    private final Integer distance;

    public InsertStationServiceCommand(Long addStationId, Long lineId, Long adjacentStationId, String direction,
                                       Integer distance) {
        this.stationId = addStationId;
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
