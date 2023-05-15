package subway.ui.dto;

import javax.validation.constraints.Positive;

public class DeleteSectionRequest {

    @Positive
    private Long lineId;
    @Positive
    private Long stationId;

    public DeleteSectionRequest() {
    }

    public DeleteSectionRequest(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
