package subway.dto;

public class SectionRequest {

    private final String lineName;
    private final String beforeStationName;
    private final String nextStationName;
    private final Integer distance;

    public SectionRequest(final String lineName, final String beforeStationName, final String nextStationName, final Integer distance) {
        this.lineName = lineName;
        this.beforeStationName = beforeStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
        this(null, null, null, null);
    }

    public String getLineName() {
        return lineName;
    }

    public String getBeforeStationName() {
        return beforeStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
