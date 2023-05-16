package subway.controller.dto.request;

public class AddInitStationToLineRequest {

    private String lineName;
    private String upStationName;
    private String downStationName;
    private Long distance;

    private AddInitStationToLineRequest() {
    }

    public AddInitStationToLineRequest(final String lineName, final String upStationName, final String downStationName,
        final Long distance) {
        this.lineName = lineName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
