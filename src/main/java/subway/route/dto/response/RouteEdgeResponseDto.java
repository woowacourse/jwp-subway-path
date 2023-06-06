package subway.route.dto.response;

public class RouteEdgeResponseDto {

    private final long sourceId;
    private final long targetId;
    private final long distance;
    private final long lineId;

    public RouteEdgeResponseDto(long sourceId, long targetId, long distance, long lineId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public long getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }
}
