package subway.dto;

public class SectionRequest {
    
    long lineId;
    long newStationId;
    long baseStationId;
    boolean direction;
    int distance;
    
    public SectionRequest(final long lineId, final long newStationId, final long baseStationId, final boolean direction,
            final int distance) {
        this.lineId = lineId;
        this.newStationId = newStationId;
        this.baseStationId = baseStationId;
        this.direction = direction;
        this.distance = distance;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    public long getNewStationId() {
        return this.newStationId;
    }
    
    public long getBaseStationId() {
        return this.baseStationId;
    }
    
    public boolean isDirection() {
        return this.direction;
    }
    
    public int getDistance() {
        return this.distance;
    }
}