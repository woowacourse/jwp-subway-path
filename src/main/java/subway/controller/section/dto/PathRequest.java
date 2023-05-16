package subway.controller.section.dto;

public class PathRequest {
    private long sourceStationId;
    private long targetStationId;

    public PathRequest(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public PathRequest() {
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}
