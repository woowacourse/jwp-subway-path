package subway.line.presentation.dto;

public class StationDeletingRequest {
    private final Long stationId;

    public StationDeletingRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
