package subway.dto.api;

import subway.dto.service.InsertStationServiceCommand;

public class StationInsertRequest {

    private final Long stationId;
    private final Long lineId;
    private final Long adjacentStationId;
    private final String direction;
    private final Integer distance;

    public StationInsertRequest(Long AddStationId, Long lineId, Long adjacentStationId, String direction,
                                Integer distance) {
        this.stationId = AddStationId;
        this.lineId = lineId;
        this.adjacentStationId = adjacentStationId;
        this.direction = direction;
        this.distance = distance;
    }

    public InsertStationServiceCommand toCommand() {
        return new InsertStationServiceCommand(stationId, lineId, adjacentStationId, direction, distance);
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
