package subway.dto.path;

import javax.validation.constraints.Positive;

public class PathSelectRequest {
    @Positive
    private final Long sourceStationId;
    @Positive
    private final Long targetStationId;

    public PathSelectRequest(final Long sourceStationId, final Long targetStationId) {
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
