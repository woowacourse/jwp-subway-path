package subway.application.dto;

public class AddStationToEndLineRequest {

    private String lineName;
    private String stationName;
    private Long distance;

    public AddStationToEndLineRequest() {
    }

    public AddStationToEndLineRequest(final String lineName, final String stationName, final Long distance) {
        this.lineName = lineName;
        this.stationName = stationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public Long getDistance() {
        return distance;
    }
}
