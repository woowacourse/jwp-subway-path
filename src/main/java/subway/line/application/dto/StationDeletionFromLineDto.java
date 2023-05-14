package subway.line.application.dto;

public class StationDeletionFromLineDto {

    private final long lineId;
    private final long stationId;

    public StationDeletionFromLineDto(long lineId, long stationId) {
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
