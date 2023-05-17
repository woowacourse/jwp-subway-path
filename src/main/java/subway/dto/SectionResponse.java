package subway.dto;

import subway.entity.SectionEntity;

public class SectionResponse {
    
    long id;
    long lineId;
    long upStationId;
    long downStationId;
    int distance;
    
    public SectionResponse(final long id, final long lineId, final long upStationId, final long downStationId,
            final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
    
    public static SectionResponse of(final SectionEntity sectionEntity) {
        return new SectionResponse(sectionEntity.getId(), sectionEntity.getLineId(), sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(), sectionEntity.getDistance());
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
