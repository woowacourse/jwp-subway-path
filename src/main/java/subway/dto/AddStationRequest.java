package subway.dto;

import java.util.Objects;

public class AddStationRequest {

    private String addStationName;
    private long lineId;
    private long upstreamId;
    private long downstreamId;
    private int distanceToUpstream;

    public AddStationRequest() {
    }

    public AddStationRequest(String addStationName, long lineId, long upstreamId, long downstreamId, int distanceToUpstream) {
        this.addStationName = addStationName;
        this.lineId = lineId;
        this.upstreamId = upstreamId;
        this.downstreamId = downstreamId;
        this.distanceToUpstream = distanceToUpstream;
    }

    public String getAddStationName() {
        return addStationName;
    }

    public long getLineId() {
        return lineId;
    }

    public long getUpstreamId() {
        return upstreamId;
    }

    public long getDownstreamId() {
        return downstreamId;
    }

    public int getDistanceToUpstream() {
        return distanceToUpstream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddStationRequest that = (AddStationRequest) o;
        return lineId == that.lineId && upstreamId == that.upstreamId && downstreamId == that.downstreamId && distanceToUpstream == that.distanceToUpstream && Objects.equals(addStationName, that.addStationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addStationName, lineId, upstreamId, downstreamId, distanceToUpstream);
    }

    @Override
    public String toString() {
        return "AddStationRequest{" +
                "addStationName='" + addStationName + '\'' +
                ", lineId=" + lineId +
                ", upstreamId=" + upstreamId +
                ", downstreamId=" + downstreamId +
                ", distanceToUpstream=" + distanceToUpstream +
                '}';
    }
}
