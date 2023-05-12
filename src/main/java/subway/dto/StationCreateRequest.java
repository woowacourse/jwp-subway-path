package subway.dto;

public class StationCreateRequest {
    private String stationName;

    public StationCreateRequest() {
    }

    public StationCreateRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
