package subway.controller.section.dto;

public class LineStationDeleteRequest {
    private long lineId;
    private long stationId;

    public LineStationDeleteRequest(long lineId, long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public LineStationDeleteRequest() {
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
