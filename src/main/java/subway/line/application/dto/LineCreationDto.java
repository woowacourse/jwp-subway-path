package subway.line.application.dto;

public class LineCreationDto {

    private final String lineName;
    private final String upstreamName;
    private final String downstreamName;
    private final int distance;
    private final int additionalFare;

    public LineCreationDto(String lineName, String upstreamName, String downstreamName, int distance, int additionalFare) {
        this.lineName = lineName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distance = distance;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }
}
