package subway.application.dto;

public class AddStationToBetweenLineRequest {

    private String lineName;
    private String stationName;
    private String upStationName;
    private String downStationName;
    private Long distance;

    public AddStationToBetweenLineRequest() {
    }

    public AddStationToBetweenLineRequest(final String lineName, final String stationName, final String upStationName,
        final String downStationName,
        final Long distance) {
        this.lineName = lineName;
        this.stationName = stationName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getDistance() {
        return distance;
    }
}
