package subway.domain;

import subway.dto.SectionRequest;

public class Section {
    
    long id;
    long lineId;
    String up;
    String down;
    int distance;
    
    public static Section from(SectionRequest sectionRequest) {
        if (sectionRequest.isDirection()) {
            return new Section(sectionRequest.getLineId(), sectionRequest.getBaseStation(),
                    sectionRequest.getNewStation(), sectionRequest.getDistance());
        }
        
        return new Section(sectionRequest.getLineId(), sectionRequest.getNewStation(), sectionRequest.getBaseStation(),
                sectionRequest.getDistance());
    }
    
    private Section(final long lineId, final String up, final String down, final int distance) {
        this.lineId = lineId;
        this.up = up;
        this.down = down;
        this.distance = distance;
    }
    
    public Section(final long id, final long lineId, final String up, final String down, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.up = up;
        this.down = down;
        this.distance = distance;
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
