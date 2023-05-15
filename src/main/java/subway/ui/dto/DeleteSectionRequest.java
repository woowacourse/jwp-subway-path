package subway.ui.dto;

public class DeleteSectionRequest {

    private Long lineId;
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
