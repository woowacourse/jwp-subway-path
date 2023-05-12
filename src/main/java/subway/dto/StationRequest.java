package subway.dto;

public class StationRequest {
    private String upStationName;
    private String downStationName;
    private int distance;
    private String lineName;

    private StationRequest() {
    }

    public StationRequest(String upStationName, String downStationName, int distance, String lineName) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
        this.lineName = lineName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    public String getLineName() {
        return lineName;
    }

    @Override
    public String toString() {
        return "StationRequest{" +
                "upStationName='" + upStationName + '\'' +
                ", downStationName='" + downStationName + '\'' +
                ", distance=" + distance +
                ", lineName='" + lineName + '\'' +
                '}';
    }
}
