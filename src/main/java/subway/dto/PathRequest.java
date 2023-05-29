package subway.dto;

public class PathRequest {

    private Long fromStationId;
    private Long toStationId;

    public PathRequest() {
    }

    public PathRequest(final Long fromStationId, final Long toStationId) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
    }

    public Long getFromStationId() {
        return fromStationId;
    }

    public Long getToStationId() {
        return toStationId;
    }
}
