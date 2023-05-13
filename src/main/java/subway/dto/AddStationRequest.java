package subway.dto;

import javax.validation.constraints.NotNull;

public class AddStationRequest {

    @NotNull
    private String addStationName;
    @NotNull
    private String lineName;
    @NotNull
    private String upstreamName;
    @NotNull
    private String downstreamName;
    @NotNull
    private int distanceToUpstream;

    public AddStationRequest(String addStationName, String lineName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.addStationName = addStationName;
        this.lineName = lineName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
    }

    public String getAddStationName() {
        return addStationName;
    }

    public String getLineName() {
        return lineName;
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
