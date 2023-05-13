package subway.application.request;

public class CreateSectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Long lineId;
    private Integer distance;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long upStationId, final Long downStationId, final Long lineId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
