package subway.ui.dto;

public class SectionDto {

    private final String upstreamName;
    private final String downstreamName;
    private final int distance;

    public SectionDto(String upstreamName, String downstreamName, int distance) {
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distance = distance;
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
