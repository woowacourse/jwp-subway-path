package subway.dto;

public class DeleteStationRequest {

    private String lineName;
    private String stationName;

    public DeleteStationRequest(String lineName, String stationName) {
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
