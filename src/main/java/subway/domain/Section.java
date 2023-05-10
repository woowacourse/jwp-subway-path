package subway.domain;

import subway.dto.SectionRequest;

public class Section {
    
    long id;
    long lineId;
    long upStationId;
    long downStationId;
    int distance;
    
    private Section(final long lineId, final long upStationId, final long downStationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
    
    public Section(final long id, final long lineId, final long upStationId, final long downStationId,
            final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
    
    public static Section from(final SectionRequest sectionRequest) {
        if (sectionRequest.isDirection()) {
            return new Section(sectionRequest.getLineId(), sectionRequest.getBaseStationId(),
                    sectionRequest.getNewStationId(), sectionRequest.getDistance());
        }
        
        return new Section(sectionRequest.getLineId(), sectionRequest.getNewStationId(),
                sectionRequest.getBaseStationId(),
                sectionRequest.getDistance());
    }
    
    public long getId() {
        return this.id;
    }
    
    public long getLineId() {
        return this.lineId;
    }
    
    public long getUpStationId() {
        return this.upStationId;
    }
    
    public long getDownStationId() {
        return this.downStationId;
    }
    
    public int getDistance() {
        return this.distance;
    }
}
