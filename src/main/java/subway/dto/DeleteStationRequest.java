package subway.dto;

public class DeleteStationRequest {

    private final long lineId;
    private final long stationId;

    public DeleteStationRequest(long lineId, long stationId) {
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
