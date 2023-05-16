package subway.route.domain;

public class RouteSegment {

    private final long sourceId;
    private final String sourceName;
    private final long destinationId;
    private final String destinationName;
    private final long lineId;
    private final String lineName;
    private final int distance;

    public RouteSegment(long sourceId, String sourceName, long destinationId, String destinationName, long lineId, String lineName, int distance) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.lineId = lineId;
        this.lineName = lineName;
        this.distance = distance;
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public String getDestinationName() {
        return destinationName;
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
}
