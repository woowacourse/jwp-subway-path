package subway.dto;

public class StationAddRequest {

    private final String line;
    private final String upLineStation;
    private final String downLineStation;
    private final int distance;

    public StationAddRequest(final String line, final String upLineStation, final String downLineStation, final int distance) {
        this.line = line;
        this.upLineStation = upLineStation;
        this.downLineStation = downLineStation;
        this.distance = distance;
    }

    public String getLine() {
        return line;
    }

    public String getUpLineStation() {
        return upLineStation;
    }

    public String getDownLineStation() {
        return downLineStation;
    }

    public int getDistance() {
        return distance;
    }
}
