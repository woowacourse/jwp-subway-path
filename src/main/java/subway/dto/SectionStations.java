package subway.dto;

import javax.validation.constraints.NotNull;

public class SectionStations {

    @NotNull
    private final Long baseStationId;
    @NotNull
    private final Long nextStationId;
    @NotNull
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
