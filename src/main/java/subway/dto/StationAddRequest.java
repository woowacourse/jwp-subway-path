package subway.dto;

public class StationAddRequest {

    private String stationName;

    public StationAddRequest() {
    }

    public StationAddRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
