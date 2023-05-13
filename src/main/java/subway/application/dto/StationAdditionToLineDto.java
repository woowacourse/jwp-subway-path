package subway.application.dto;

public class StationAdditionToLineDto {

    private final long lineId;
    private final String stationName;
    private final String upstreamName;
    private final String downstreamName;
    private final int distanceToUpstream;

    public StationAdditionToLineDto(long lineId, String stationName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.lineId = lineId;
        this.stationName = stationName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
    }

    public long getLineId() {
        return lineId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public String getDownstreamName() {
        return downstreamName;
    }

    public int getDistanceToUpstream() {
        return distanceToUpstream;
    }
}
