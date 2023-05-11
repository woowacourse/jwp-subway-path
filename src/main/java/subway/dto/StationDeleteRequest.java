package subway.dto;

public class StationDeleteRequest {

    private Long lineId;
    private Long stationId;

    public StationDeleteRequest() {
    }

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
