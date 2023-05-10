package subway.dto;

public class StationUpdateRequest {
    private String stationName;

    public StationUpdateRequest() {
    }

    public StationUpdateRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
