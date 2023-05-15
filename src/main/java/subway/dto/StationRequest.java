package subway.dto;

public class StationRequest {

    private String upStation;
    private String downStation;
    private int distance;

    public StationRequest() {
    }

    public StationRequest(String upStation, String downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
