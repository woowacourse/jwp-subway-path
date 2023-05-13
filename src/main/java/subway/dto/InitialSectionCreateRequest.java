package subway.dto;

public class InitialSectionCreateRequest {

    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public InitialSectionCreateRequest(
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
