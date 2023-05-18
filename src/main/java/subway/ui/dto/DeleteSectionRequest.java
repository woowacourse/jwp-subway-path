package subway.ui.dto;

import javax.validation.constraints.Positive;

public class DeleteSectionRequest {

    @Positive
    private Long stationId;

    public DeleteSectionRequest() {
    }

    public DeleteSectionRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
