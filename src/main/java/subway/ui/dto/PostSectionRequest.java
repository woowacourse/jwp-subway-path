package subway.ui.dto;

import javax.validation.constraints.Positive;

public class PostSectionRequest {

    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private Long lineId;
    @Positive
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
