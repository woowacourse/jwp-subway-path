package subway.ui.dto;

import javax.validation.constraints.Positive;

public class PostSectionRequest {

    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private Integer distance;

    public PostSectionRequest() {
    }

    public PostSectionRequest(Long upStationId, Long downStationId, Integer distance) {
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
