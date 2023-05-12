package subway.dto;

public class StationDeleteRequest {
    private Long stationId;

    public StationDeleteRequest() {
    }

    public StationDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
