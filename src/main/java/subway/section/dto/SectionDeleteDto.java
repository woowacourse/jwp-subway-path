package subway.section.dto;

public class SectionDeleteDto {

    private final Long lineId;
    private final Long stationId;
    private final String stationName;

    public SectionDeleteDto(final Long lineId, final Long stationId, final String stationName) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
