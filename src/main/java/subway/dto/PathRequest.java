package subway.dto;

public class PathRequest {

    private final Long fromStationId;
    private final Long toStationId;

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
