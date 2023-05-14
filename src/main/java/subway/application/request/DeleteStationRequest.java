package subway.application.request;

public class DeleteStationRequest {

    private final String stationName;
    private final String lineName;

    public DeleteStationRequest(final String stationName, final String lineName) {
        this.stationName = stationName;
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getLineName() {
        return lineName;
    }
}
