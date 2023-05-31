package subway.line.application.dto;

public class StationAdditionToLineDto {

    private final long lineId;
    private final String stationName;
    private final Long upstreamId;
    private final Long downstreamId;
    private final int distanceToUpstream;

    public StationAdditionToLineDto(long lineId, String stationName, Long upstreamId, Long downstreamId, int distanceToUpstream) {
        this.lineId = lineId;
        this.stationName = stationName;
        this.upstreamId = upstreamId;
        this.downstreamId = downstreamId;
        this.distanceToUpstream = distanceToUpstream;
    }

    public long getLineId() {
        return lineId;
    }

    public String getStationName() {
        return stationName;
    }

    public Long getUpstreamId() {
        return upstreamId;
    }

    public Long getDownstreamId() {
        return downstreamId;
    }

    public int getDistanceToUpstream() {
        return distanceToUpstream;
    }
}
