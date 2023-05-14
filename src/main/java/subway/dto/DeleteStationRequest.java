package subway.dto;

public class DeleteStationRequest {

    private String stationName;

    public DeleteStationRequest(String stationName) {
        this.stationName = stationName;
    }

    public DeleteStationRequest() {
    }

    public String getStationName() {
        return stationName;
    }
}
