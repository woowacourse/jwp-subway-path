package subway.dto;

public class RouteRequest {
    
    private final long sourceStationId;
    private final long targetStationId;
    
    public RouteRequest(final long sourceStationId, final long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }
    
    public long getSourceStationId() {
        return this.sourceStationId;
    }
    
    public long getTargetStationId() {
        return this.targetStationId;
    }
}
