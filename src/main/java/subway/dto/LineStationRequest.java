package subway.dto;

public class LineStationRequest {
    private String upBoundStationName;
    private String downBoundStationName;
    private int distance;

    public LineStationRequest() {
    }

    public LineStationRequest(String upBoundStationName, String downBoundStationName, int distance) {
        this.upBoundStationName = upBoundStationName;
        this.downBoundStationName = downBoundStationName;
        this.distance = distance;
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
