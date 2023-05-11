package subway.dto;

public class SectionRequest {

    private final String beforeStationName;
    private final String nextStationName;
    private final Integer distance;

    public SectionRequest(final String beforeStationName, final String nextStationName, final Integer distance) {
        this.beforeStationName = beforeStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
        this(null, null, null);
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
