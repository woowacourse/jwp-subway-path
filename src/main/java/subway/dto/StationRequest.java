package subway.dto;

public class StationRequest {
    private String upStation;
    private String downStation;
    private int distance;
    private long lineId;

    public StationRequest() {

    }

    public StationRequest(final String upStation, final String downStation, final int distance, final long lineId) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineId = lineId;
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

    public long getLineId() {
        return lineId;
    }
}
