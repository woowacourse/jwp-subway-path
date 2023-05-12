package subway.dto;

public class SectionStations {

    private final Long baseStationId;
    private final Long nextStationId;
    private final Integer distance;

    public SectionStations(final Long baseStationId, final Long nextStationId, final Integer distance) {
        this.baseStationId = baseStationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public Long getBaseStationId() {
        return baseStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
