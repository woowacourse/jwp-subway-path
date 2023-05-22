package subway.dto;

import java.util.Objects;

public class AddStationRequest {

    private String addStationName;
    private String lineName;
    private String upstreamName;
    private String downstreamName;
    private int distanceToUpstream;

    public AddStationRequest() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddStationRequest that = (AddStationRequest) o;
        return distanceToUpstream == that.distanceToUpstream && Objects.equals(addStationName, that.addStationName) && Objects.equals(lineName, that.lineName) && Objects.equals(upstreamName, that.upstreamName) && Objects.equals(downstreamName, that.downstreamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addStationName, lineName, upstreamName, downstreamName, distanceToUpstream);
    }

    @Override
    public String toString() {
        return "AddStationRequest{" +
                "addStationName='" + addStationName + '\'' +
                ", lineName='" + lineName + '\'' +
                ", upstreamName='" + upstreamName + '\'' +
                ", downstreamName='" + downstreamName + '\'' +
                ", distanceToUpstream=" + distanceToUpstream +
                '}';
    }
}
