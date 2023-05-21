package subway.application.core.service.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class EnrollStationCommand {

    @NotNull
    private final Long lineId;
    @NotNull
    private final Long upBound;
    @NotNull
    private final Long downBound;
    @Positive
    private final Integer distance;

    public EnrollStationCommand(Long lineId, Long upBound, Long downBound, Integer distance) {
        this.lineId = lineId;
        this.upBound = upBound;
        this.downBound = downBound;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpBound() {
        return upBound;
    }

    public Long getDownBound() {
        return downBound;
    }

    public Integer getDistance() {
        return distance;
    }
}
