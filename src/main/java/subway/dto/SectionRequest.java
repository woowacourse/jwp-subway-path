package subway.dto;

public class SectionRequest {

    private final Long lineId;
    private final String beforeStationName;
    private final String nextStationName;
    private final Integer distance;

    public SectionRequest(final Long lineId, final String beforeStationName, final String nextStationName, final Integer distance) {
        this.lineId = lineId;
        this.beforeStationName = beforeStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
        this(null, null, null, null);
    }

    public Long getLineId() {
        return lineId;
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
