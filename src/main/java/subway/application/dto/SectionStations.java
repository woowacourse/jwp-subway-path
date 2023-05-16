package subway.application.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SectionStations {

    private final Long leftStationId;
    private final Long rightStationId;
    private final Integer distance;

    public SectionStations(final Long leftStationId, final Long rightStationId, final Integer distance) {
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
