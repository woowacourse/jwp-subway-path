package subway.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class SectionAtLastRequest {

    private Long lineId;
    private Long stationId;
    private Long originalLastStationId;
    @Positive
    private Integer distance;

    public SectionAtLastRequest() {

    }

    public SectionAtLastRequest(Long lineId, Long stationId, Long originalLastStationId, Integer distance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.originalLastStationId = originalLastStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getOriginalLastStationId() {
        return originalLastStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
