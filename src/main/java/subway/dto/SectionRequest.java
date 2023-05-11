package subway.dto;

public class SectionRequest {

    private final Long lineId;
    private final SectionStations sectionStations;

    public SectionRequest(final Long lineId, final SectionStations sectionStations) {
        this.lineId = lineId;
        this.sectionStations = sectionStations;
    }

    public Long baseStationId() {
        return sectionStations.getBaseStationId();
    }

    public Long nextStationId() {
        return sectionStations.getNextStationId();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSectionStations() {
        return sectionStations;
    }
}
