package subway.controller.section.dto;

public class LineStationDeleteRequest {
    private Long stationId;

    public LineStationDeleteRequest(long stationId) {
        this.stationId = stationId;
    }

    public LineStationDeleteRequest() {
    }

    public Long getStationId() {
        return stationId;
    }
}
