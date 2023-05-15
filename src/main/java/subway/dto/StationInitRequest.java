package subway.dto;

public class StationInitRequest {

    private final String lineName;
    private final String upBoundStationName;
    private final String downBoundStationName;
    private final int distance;

    public StationInitRequest(String lineName, String upBoundStationName, String downBoundStationName, int distance) {
        this.lineName = lineName;
        this.upBoundStationName = upBoundStationName;
        this.downBoundStationName = downBoundStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpBoundStationName() {
        return upBoundStationName;
    }

    public String getDownBoundStationName() {
        return downBoundStationName;
    }

    public int getDistance() {
        return distance;
    }
}
