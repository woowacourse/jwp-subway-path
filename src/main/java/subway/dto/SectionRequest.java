package subway.dto;

public class SectionRequest {
    long lineId;
    String newStation;
    String baseStation;
    boolean direction;
    int distance;
    
    public SectionRequest(final long lineId, final String newStation, final String baseStation, final boolean direction,
            final int distance) {
        this.lineId = lineId;
        this.newStation = newStation;
        this.baseStation = baseStation;
        this.direction = direction;
        this.distance = distance;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    public String getNewStation() {
        return this.newStation;
    }
    
    public String getBaseStation() {
        return this.baseStation;
    }
    
    public boolean isDirection() {
        return this.direction;
    }
    
    public int getDistance() {
        return this.distance;
    }
}
