package subway.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class InitSectionRequest {

    private Long lineId;
    private Long upwardId;
    private Long downwardId;
    @Positive
    private int distance;

    public InitSectionRequest() {
    }

    public InitSectionRequest(Long lineId, Long upwardId, Long downwardId, int distance) {
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

    public int getDistance() {
        return distance;
    }
}
