package subway.dto;

public class StationDto {
    private final long stationId;
    private final String stationName;

    public StationDto(long stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }
}
