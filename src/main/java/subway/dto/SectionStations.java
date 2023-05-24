package subway.dto;

import javax.validation.constraints.NotNull;

public class SectionStations {

    @NotNull
    private Long baseStationId;
    @NotNull
    private Long nextStationId;
    @NotNull
    private Integer distance;

    public SectionStations() {
    }

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
