package subway.controller.dto;

public class RemoveStationOnLineRequest {

    private String lineName;
    private String stationName;

    public RemoveStationOnLineRequest() {
    }

    public RemoveStationOnLineRequest(final String lineName, final String stationName) {
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
