package subway.dto;

public class StationRequest {

    private String upStationName;
    private String downStationName;
    private Integer distance;

    public StationRequest() {
    }

    public StationRequest(final String upStationName, final String downStationName, final Integer distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
