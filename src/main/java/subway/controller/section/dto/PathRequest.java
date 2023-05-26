package subway.controller.section.dto;

public class PathRequest {
    private Long sourceStationId;
    private Long targetStationId;

    public PathRequest(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public PathRequest() {
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
