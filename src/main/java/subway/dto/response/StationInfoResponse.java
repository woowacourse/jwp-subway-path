package subway.dto.response;

public class StationInfoResponse {
    private final long stationId;
    private final String stationName;

    public StationInfoResponse(long stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
