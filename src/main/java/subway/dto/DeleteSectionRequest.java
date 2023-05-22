package subway.dto;

import javax.validation.constraints.Min;

public class DeleteSectionRequest {
    
    @Min(1)
    private final long stationId;
    @Min(1)
    private final long lineId;
    
    public DeleteSectionRequest(final long stationId, final long lineId) {
        this.stationId = stationId;
        this.lineId = lineId;
    }
    
    public long getStationId() {
        return this.stationId;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    
}
