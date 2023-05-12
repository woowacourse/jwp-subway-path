package subway.section.dto;

public class SectionDeleteRequest {

    private Long lineId;
    private Long stationId;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final Long lineId, final Long stationId) {
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
