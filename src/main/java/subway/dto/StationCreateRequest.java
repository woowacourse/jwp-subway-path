package subway.dto;

public class StationCreateRequest {
    private String stationName;

    public StationCreateRequest() {
    }

    public StationCreateRequest(String name) {
        this.stationName = name;
    }

    public String getStationName() {
        return stationName;
    }
}
