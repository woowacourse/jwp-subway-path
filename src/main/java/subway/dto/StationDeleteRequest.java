package subway.dto;

public class StationDeleteRequest {
    private final String lineName;
    private final String stationName;

    public StationDeleteRequest(String lineName, String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }
}
