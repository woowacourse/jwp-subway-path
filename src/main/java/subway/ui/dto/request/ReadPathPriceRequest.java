package subway.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class ReadPathPriceRequest {

    @Schema(description = "출발역 ID")
    @NotNull
    private final Long sourceStationId;

    @Schema(description = "도착역 ID")
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
