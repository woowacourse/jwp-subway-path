package subway.controller.dto;

public class AddInitStationToLine {

    private String lineName;
    private String upStationName;
    private String downStationName;
    private Long distance;

    public AddInitStationToLine() {
    }

    public AddInitStationToLine(final String lineName, final String upStationName, final String downStationName,
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
