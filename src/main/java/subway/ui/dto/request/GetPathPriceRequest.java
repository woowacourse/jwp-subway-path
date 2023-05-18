package subway.ui.dto.request;

import javax.validation.constraints.NotNull;

public class GetPathPriceRequest {

    @NotNull
    private final Long sourceStationId;
    @NotNull
    private final Long targetStationId;

    private GetPathPriceRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static GetPathPriceRequest of(final Long sourceStationId, final Long targetStationId) {
        return new GetPathPriceRequest(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
