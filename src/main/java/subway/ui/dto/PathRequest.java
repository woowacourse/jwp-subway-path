package subway.ui.dto;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive
    private final Long sourceStationId;
    @Positive
    private final Long targetStationId;

    public PathRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
