package subway.controller.dto;

public class RemoveStationOnLineRequest {

    private final String lineName;
    private final String stationName;

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
