package subway.dto;

import javax.validation.constraints.NotNull;
import subway.domain.Direction;

public class SectionRequest {

    @NotNull
    private final SectionStations sectionStations;
    @NotNull
    private final Direction direction;

    public SectionRequest(final SectionStations sectionStations, final String direction) {
        this.sectionStations = sectionStations;
        this.direction = Direction.convert(direction);
    }

    public SectionStations getSectionStations() {
        return sectionStations;
    }

    public Long baseStationId() {
        return sectionStations.getBaseStationId();
    }

    public Long nextStationId() {
        return sectionStations.getNextStationId();
    }

    public Direction getDirection() {
        return direction;
    }
}
