package subway.ui.dto.request;

import javax.validation.constraints.NotNull;

public class ReadPathPriceRequest {

    @NotNull
    private final Long sourceStationId;
    @NotNull
    private final Long targetStationId;

    private ReadPathPriceRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static ReadPathPriceRequest of(final Long sourceStationId, final Long targetStationId) {
        return new ReadPathPriceRequest(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
