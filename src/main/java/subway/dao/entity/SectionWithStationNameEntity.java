package subway.dao.entity;

public class SectionWithStationNameEntity {
    private final Long sectionId;
    private final StationEntity upStationEntity;
    private final StationEntity downStationEntity;
    private final int distance;

    public SectionWithStationNameEntity(final Long sectionId, final StationEntity upStationEntity,
                                        final StationEntity downStationEntity, final int distance) {
        this.sectionId = sectionId;
        this.upStationEntity = upStationEntity;
        this.downStationEntity = downStationEntity;
        this.distance = distance;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public StationEntity getUpStationEntity() {
        return upStationEntity;
    }

    public StationEntity getDownStationEntity() {
        return downStationEntity;
    }

    public int getDistance() {
        return distance;
    }
}
