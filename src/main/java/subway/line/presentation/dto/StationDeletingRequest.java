package subway.line.presentation.dto;

public class StationDeletingRequest {
    private Long stationId;

    public StationDeletingRequest() {
    }

    public StationDeletingRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
