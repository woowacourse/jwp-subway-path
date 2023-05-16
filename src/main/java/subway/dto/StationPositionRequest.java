package subway.dto;

public class StationPositionRequest {

    private final Long lineId;
    private final Long upStationId;
    private final Integer upStationDistance;
    private final Long downStationId;
    private final Integer downStationDistance;

    public StationPositionRequest(final Long lineId, final Long upStationId, final Integer upStationDistance, final Long downStationId, final Integer downStationDistance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.upStationDistance = upStationDistance;
        this.downStationId = downStationId;
        this.downStationDistance = downStationDistance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Integer getUpStationDistance() {
        return upStationDistance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDownStationDistance() {
        return downStationDistance;
    }
}
