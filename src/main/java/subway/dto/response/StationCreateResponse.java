package subway.dto.response;

public class StationCreateResponse {
    private final Long stationId;
    private final String stationName;

    public StationCreateResponse(Long stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
