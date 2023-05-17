package subway.ui.dto.request;

public class GetPathRequest {

    private final Long sourceStationId;
    private final Long targetStationId;

    private GetPathRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static GetPathRequest of(final Long sourceStationId, final Long targetStationId) {
        return new GetPathRequest(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
