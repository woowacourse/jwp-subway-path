package subway.ui.dto;

import javax.validation.constraints.NotNull;

public class StationAdditionRequest {

    @NotNull
    private String stationName;
    @NotNull
    private String upstreamName;
    @NotNull
    private String downstreamName;
    @NotNull
    private int distanceToUpstream;

    public StationAdditionRequest(String stationName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.stationName = stationName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
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
