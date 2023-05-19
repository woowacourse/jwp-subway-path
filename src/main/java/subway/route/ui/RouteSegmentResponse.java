package subway.route.ui;

import subway.route.domain.RouteSegment;

public class RouteSegmentResponse {

    private final long upstreamId;
    private final String upstreamName;
    private final long downstreamId;
    private final String downstreamName;
    private final long lineId;
    private final String lineName;
    private final int distance;

    private RouteSegmentResponse(long upstreamId, String upstreamName, long downstreamId, String downstreamName, long lineId, String lineName, int distance) {
        this.upstreamId = upstreamId;
        this.upstreamName = upstreamName;
        this.downstreamId = downstreamId;
        this.downstreamName = downstreamName;
        this.lineId = lineId;
        this.lineName = lineName;
        this.distance = distance;
    }

    public static RouteSegmentResponse from(RouteSegment routeSegment) {
        return new RouteSegmentResponse(
                routeSegment.getUpstreamId(),
                routeSegment.getUpstreamName(),
                routeSegment.getDownstreamId(),
                routeSegment.getDownstreamName(),
                routeSegment.getLineId(),
                routeSegment.getLineName(),
                routeSegment.getDistance()
        );
    }

    public long getUpstreamId() {
        return upstreamId;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public long getDownstreamId() {
        return downstreamId;
    }

    public String getDownstreamName() {
        return downstreamName;
    }

    public long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "RouteSegmentResponse{" +
                "upstreamId=" + upstreamId +
                ", upstreamName='" + upstreamName + '\'' +
                ", downstreamId=" + downstreamId +
                ", downstreamName='" + downstreamName + '\'' +
                ", lineId=" + lineId +
                ", lineName='" + lineName + '\'' +
                ", distance=" + distance +
                '}';
    }
}
