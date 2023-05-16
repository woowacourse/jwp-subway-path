package subway.application.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotNull
    private final Long lineId;
    @NotNull
    private final SectionStations section;

    public SectionRequest(final Long lineId, final SectionStations section) {
        this.lineId = lineId;
        this.section = section;
    }

    public Long leftStationId() {
        return section.getLeftStationId();
    }

    public Long rightStationId() {
        return section.getRightStationId();
    }

    public Integer distance() {
        return section.getDistance();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSection() {
        return section;
    }
}
