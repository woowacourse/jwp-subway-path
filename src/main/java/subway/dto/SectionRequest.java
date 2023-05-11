package subway.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class SectionRequest {

    private Long lineId;
    private Long stationId;
    private Long upwardId;
    private Long downwardId;
    @Positive
    private Integer upwardDistance;
    @Positive
    private Integer downwardDistance;

    public SectionRequest() {
    }

    public SectionRequest(Long lineId, Long stationId, Long upwardId, Long downwardId,
                          Integer upwardDistance, Integer downwardDistance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
        this.upwardDistance = upwardDistance;
        this.downwardDistance = downwardDistance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getUpwardId() {
        return upwardId;
    }

    public Long getDownwardId() {
        return downwardId;
    }

    public Integer getUpwardDistance() {
        return upwardDistance;
    }

    public Integer getDownwardDistance() {
        return downwardDistance;
    }
}
