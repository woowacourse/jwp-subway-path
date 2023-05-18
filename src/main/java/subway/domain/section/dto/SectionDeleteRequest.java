package subway.domain.section.dto;

import javax.validation.constraints.NotNull;

public class SectionDeleteRequest {

    @NotNull
    private Long lineId;
    @NotNull
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
