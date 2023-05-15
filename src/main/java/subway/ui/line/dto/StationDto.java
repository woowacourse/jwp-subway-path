package subway.ui.line.dto;

public class StationDto {

    private final Long stationId;
    private final String stationName;

    public StationDto(final Long stationId, final String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
