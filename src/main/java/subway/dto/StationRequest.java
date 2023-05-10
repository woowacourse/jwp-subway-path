package subway.dto;

public class StationRequest {
    private String startStation;
    private String endStation;
    private int distance;
    private long lineId;

    public StationRequest() {

    }

    public StationRequest(final String startStation, final String endStation, final int distance, final long lineId) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }
}
