package subway.dto;

import javax.validation.constraints.NotNull;

public class AddStationRequest {

    @NotNull
    private String stationToAddName;
    @NotNull
    private String upstreamName;
    @NotNull
    private String downstreamName;
    @NotNull
    private int distanceToUpstream;

    public AddStationRequest(String stationToAddName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.stationToAddName = stationToAddName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
    }

    public String getStationToAddName() {
        return stationToAddName;
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
