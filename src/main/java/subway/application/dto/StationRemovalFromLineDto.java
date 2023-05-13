package subway.application.dto;

public class StationRemovalFromLineDto {

    private final long lineId;
    private final long stationId;

    public StationRemovalFromLineDto(long lineId, long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
