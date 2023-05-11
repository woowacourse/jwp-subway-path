package subway.dto;

public class StationRequest {
    private String upStationName;
    private String downStationName;
    private String lineName;
    private String lineColor;
    private Integer distance;

    public StationRequest() {
    }

    public StationRequest(final String upStationName, final String downStationName, final String lineName, final String lineColor, final Integer distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "StationRequest{" +
                "upStationName='" + upStationName + '\'' +
                ", downStationName='" + downStationName + '\'' +
                ", lineName='" + lineName + '\'' +
                ", lineColor='" + lineColor + '\'' +
                ", distance=" + distance +
                '}';
    }
}
