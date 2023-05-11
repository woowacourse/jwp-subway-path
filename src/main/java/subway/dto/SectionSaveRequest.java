package subway.dto;

public class SectionSaveRequest {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionSaveRequest() {
    }

    public SectionSaveRequest(
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
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
