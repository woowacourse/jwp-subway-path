package subway.dto;

import javax.validation.constraints.Min;

public class RouteRequest {
    
    @Min(1)
    private final long sourceStationId;
    @Min(1)
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
