package subway.application.request;

public class DeleteStationRequest {

    private String stationName;
    private String lineName;

    public DeleteStationRequest() {
    }

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
