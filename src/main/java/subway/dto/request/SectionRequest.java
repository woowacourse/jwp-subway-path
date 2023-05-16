package subway.dto.request;

import javax.validation.constraints.Positive;

public class SectionRequest {

    private Long lineId;
    private Long upwardId;
    private Long downwardId;
    @Positive
    private Integer distance;

    public SectionRequest(Long lineId, Long upwardId, Long downwardId, Integer distance) {
        this.lineId = lineId;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardId() {
        return upwardId;
    }

    public Long getDownwardId() {
        return downwardId;
    }

    public Integer getDistance() {
        return distance;
    }
}
