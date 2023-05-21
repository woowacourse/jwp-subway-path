package subway.line.domain.section.dto;

import subway.line.domain.section.domain.Distance;

public class SectionSavingRequest {
    private final Long previousStationId;
    private final Long nextStationId;
    private final Distance distance;
    private final boolean isDown;

    public SectionSavingRequest(Long previousStationId, Long nextStationId, Distance distance, boolean isDown) {
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
        this.isDown = isDown;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean isDown() {
        return isDown;
    }
}
