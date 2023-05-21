package subway.dto.request;

public class PathRequest {

    private final Long startStationId;
    private final Long endStationId;

    public PathRequest(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
