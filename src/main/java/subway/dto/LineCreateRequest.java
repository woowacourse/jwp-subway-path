package subway.dto;

public class LineCreateRequest {
    private final String upLineStation;
    private final String downLineStation;
    private final String line;
    private final double distance;

    public LineCreateRequest(final String upLineStation, final String downLineStation, final String line, final double distance) {
        this.upLineStation = upLineStation;
        this.downLineStation = downLineStation;
        this.line = line;
        this.distance = distance;
    }

    public String getUpLineStation() {
        return upLineStation;
    }

    public String getDownLineStation() {
        return downLineStation;
    }

    public String getLine() {
        return line;
    }

    public double getDistance() {
        return distance;
    }
}
