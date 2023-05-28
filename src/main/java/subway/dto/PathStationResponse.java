package subway.dto;

public class PathStationResponse {
    private Long stationId;
    private String stationName;

    private PathStationResponse() {
    }

    public PathStationResponse(final Long stationId, final String stationName) {
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
