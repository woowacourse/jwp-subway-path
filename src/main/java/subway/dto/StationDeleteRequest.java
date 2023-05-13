package subway.dto;

public class StationDeleteRequest {

    private final Long lineId;
    private final Long stationId;

    public StationDeleteRequest(final Long lineId, final Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
