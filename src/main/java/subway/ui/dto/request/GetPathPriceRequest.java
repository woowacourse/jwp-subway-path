package subway.ui.dto.request;

public class GetPathPriceRequest {

    private final Long sourceStationId;
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
