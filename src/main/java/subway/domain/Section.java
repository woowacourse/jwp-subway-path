package subway.domain;

import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

public class Section {
    
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private long id;
    
    public Section(final long lineId, final long upStationId, final long downStationId, final int distance) {
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
        final Direction direction = Direction.of(sectionRequest.getDirection());
        if (direction.isDown()) {
            return new Section(sectionRequest.getLineId(), sectionRequest.getBaseStationId(),
                    sectionRequest.getNewStationId(), sectionRequest.getDistance());
        }
        return new Section(sectionRequest.getLineId(), sectionRequest.getNewStationId(),
                sectionRequest.getBaseStationId(),
                sectionRequest.getDistance());
    }
    
    public static Section from(final SectionResponse sectionResponse) {
        return new Section(sectionResponse.getId(), sectionResponse.getLineId(), sectionResponse.getUpStationId(),
                sectionResponse.getDownStationId(), sectionResponse.getDistance());
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
