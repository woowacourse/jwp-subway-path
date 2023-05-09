package subway.dto;

import subway.domain.Section;

public class SectionResponse {
    long id;
    long lineId;
    String up;
    String down;
    int distance;
    
    public SectionResponse(final long id, final long lineId, final String up, final String down, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.up = up;
        this.down = down;
        this.distance = distance;
    }
    
    public static SectionResponse of(Section section){
        return new SectionResponse(section.getId(),section.getLineId(),section.getUp(),section.getDown(),section.getDistance());
    }
    
    public long getId() {
        return this.id;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    public String getUp() {
        return this.up;
    }
    
    public String getDown() {
        return this.down;
    }
    
    public int getDistance() {
        return this.distance;
    }
}
