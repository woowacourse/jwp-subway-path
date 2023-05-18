package subway.dto;

public class StationToLineRequest {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public StationToLineRequest() {
    }

    public StationToLineRequest(final Long upStationId, final Long downStationId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
