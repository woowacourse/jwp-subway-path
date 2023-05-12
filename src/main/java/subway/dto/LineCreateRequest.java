package subway.dto;

public class LineCreateRequest {
    private String lineName;
    private long upStationId;
    private long downStationId;
    private int distance;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String lineName, long upStationId, long downStationId, int distance) {
        this.lineName = lineName;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "LineCreateRequest{" +
                "lineName='" + lineName + '\'' +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }
}
