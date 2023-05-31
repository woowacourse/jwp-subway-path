package subway.route.domain;

import subway.line.domain.LineMetadata;

public class RouteSegment {

    private final long upstreamId;
    private final String upstreamName;
    private final long downstreamId;
    private final String downstreamName;
    private final long lineId;
    private final LineMetadata lineMetadata;
    private final int distance;

    public RouteSegment(long upstreamId, String upstreamName, long downstreamId, String downstreamName, long lineId, LineMetadata lineMetadata, int distance) {
        this.upstreamId = upstreamId;
        this.upstreamName = upstreamName;
        this.downstreamId = downstreamId;
        this.downstreamName = downstreamName;
        this.lineId = lineId;
        this.lineMetadata = lineMetadata;
        this.distance = distance;
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
        return lineMetadata.getName();
    }

    public int getLineAdditionalFare() {
        return lineMetadata.getAdditionalFare();
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "RouteSegment{" +
                "upstreamId=" + upstreamId +
                ", upstreamName='" + upstreamName + '\'' +
                ", downstreamId=" + downstreamId +
                ", downstreamName='" + downstreamName + '\'' +
                ", lineId=" + lineId +
                ", lineMetadata=" + lineMetadata +
                ", distance=" + distance +
                '}';
    }
}
