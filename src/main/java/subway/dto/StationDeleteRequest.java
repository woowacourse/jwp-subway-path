package subway.dto;

public class StationDeleteRequest {
    private Long stationId;

    public StationDeleteRequest() {
    }

    public StationDeleteRequest(final Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
