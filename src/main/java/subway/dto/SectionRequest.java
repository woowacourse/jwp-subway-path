package subway.dto;

public class SectionRequest {

    private final Long lineId;
    private final SectionStations sectionStations;

    public SectionRequest(final Long lineId, final SectionStations sectionStations) {
        this.lineId = lineId;
        this.sectionStations = sectionStations;
    }

    public Long getBaseStationId() {
        return sectionStations.getBaseStationId();
    }

    public Long getNextStationId() {
        return sectionStations.getNextStationId();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSectionStations() {
        return sectionStations;
    }
}
