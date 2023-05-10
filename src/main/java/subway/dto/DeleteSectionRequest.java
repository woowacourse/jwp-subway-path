package subway.dto;

public class DeleteSectionRequest {
    
    private final long stationId;
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
