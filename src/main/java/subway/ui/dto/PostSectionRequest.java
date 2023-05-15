package subway.ui.dto;

public class PostSectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long lineId;
    private Integer distance;

    public PostSectionRequest() {
    }

    public PostSectionRequest(Long upStationId, Long downStationId, Long lineId, Integer distance) {
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
