package subway.application.request;

public class CreateStationRequest {
    private String stationName;

    public CreateStationRequest() {
    }

    public CreateStationRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
