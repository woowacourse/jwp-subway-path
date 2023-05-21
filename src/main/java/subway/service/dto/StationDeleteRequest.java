package subway.service.dto;

public class StationDeleteRequest {

    private String lineName;
    private String stationName;

    private StationDeleteRequest() {
    }

    public StationDeleteRequest(final String lineName, final String stationName) {
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
