package subway.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class SectionDeleteRequest {

    private Long stationId;
    private Long lineId;

    public SectionDeleteRequest() {

    }

    public SectionDeleteRequest(Long stationId, Long lineId) {
        this.stationId = stationId;
        this.lineId = lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getLineId() {
        return lineId;
    }
}
