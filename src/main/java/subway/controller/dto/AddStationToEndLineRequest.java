package subway.controller.dto;

public class AddStationToEndLineRequest {

    private String lineName;
    private String stationName;
    private Long distance;

    private AddStationToEndLineRequest() {
    }

    public AddStationToEndLineRequest(final String lineName, final String stationName, final Long distance) {
        this.lineName = lineName;
        this.stationName = stationName;
        this.distance = distance;
    }

    public static AddStationToEndLineRequest from(final AddStationToLineRequest addStationToLineRequest) {
        return new AddStationToEndLineRequest(addStationToLineRequest.getLineName(),
            addStationToLineRequest.getStationName(), addStationToLineRequest.getDistance());
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
