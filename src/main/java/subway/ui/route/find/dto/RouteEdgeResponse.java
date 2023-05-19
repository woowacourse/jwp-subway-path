package subway.ui.route.find.dto;

public class RouteEdgeResponse {

    private long sourceId;
    private long targetId;
    private long distance;
    private long lineId;

    private RouteEdgeResponse() {
    }

    public RouteEdgeResponse(final long sourceId, final long targetId, final long distance, final long lineId) {
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
