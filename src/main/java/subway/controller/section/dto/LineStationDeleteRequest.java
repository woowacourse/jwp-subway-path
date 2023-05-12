package subway.controller.section.dto;

public class LineStationDeleteRequest {
    private long stationId;

    public LineStationDeleteRequest(long stationId) {
        this.stationId = stationId;
    }

    public LineStationDeleteRequest() {
    }

    public long getStationId() {
        return stationId;
    }
}
