package subway.application.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotNull
    private final Long lineId;
    @NotNull
    private final SectionStations sectionStations;

    public SectionRequest(final Long lineId, final SectionStations sectionStations) {
        this.lineId = lineId;
        this.sectionStations = sectionStations;
    }

    public Long leftStationId() {
        return sectionStations.getLeftStationId();
    }

    public Long rightStationId() {
        return sectionStations.getRightStationId();
    }

    public Integer distance() {
        return sectionStations.getDistance();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSectionStations() {
        return sectionStations;
    }
}
