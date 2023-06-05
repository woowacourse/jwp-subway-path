package subway.route.find.dto;

import javax.validation.constraints.Positive;

public class RouteFindRequest {

    @Positive
    private Long sourceId;

    @Positive
    private Long targetId;

    private RouteFindRequest() {
    }

    public RouteFindRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
