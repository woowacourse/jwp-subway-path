package subway.dto.request;

public class LineCreateRequest {
    private String lineName;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

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
