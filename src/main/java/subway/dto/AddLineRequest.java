package subway.dto;

public class AddLineRequest {

    private String lineName;
    private String upstreamName;
    private String downstreamName;
    private int distance;

    public AddLineRequest() {
    }

    public AddLineRequest(String lineName, String upstreamName, String downstreamName, int distance) {
        this.lineName = lineName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }
}
