package subway.dto;

import subway.domain.Direction;

public class SectionRequest {

    private final Long lineId;
    private final SectionStations sectionStations;
    private final SectionDirection sectionDirection;

    public SectionRequest(final Long lineId, final SectionStations sectionStations,
                          final SectionDirection sectionDirection) {
        this.lineId = lineId;
        this.sectionStations = sectionStations;
        this.sectionDirection = sectionDirection;
    }

    public Long baseStationId() {
        return sectionStations.getBaseStationId();
    }

    public Long nextStationId() {
        return sectionStations.getNextStationId();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSectionStations() {
        return sectionStations;
    }

    public SectionDirection getSectionDirection() {
        return sectionDirection;
    }

    public Direction direction() {
        return sectionDirection.getDirection();
    }
}
