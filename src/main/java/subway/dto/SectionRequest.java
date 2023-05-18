package subway.dto;

import javax.validation.constraints.NotNull;
import subway.domain.vo.Direction;

public class SectionRequest {

    @NotNull
    private SectionStations sectionStations;
    @NotNull
    private Direction direction;

    public SectionRequest() {
    }

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
