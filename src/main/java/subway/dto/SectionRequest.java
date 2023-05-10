package subway.dto;

public class SectionRequest {
    private Integer distance;
    private Long upStationId;
    private Long downStationId;
    private Long lineId;

    private SectionRequest() {
    }

    public SectionRequest(Integer distance, Long upStationId, Long downStationId, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
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

    public Long getLineId() {
        return lineId;
    }
}
