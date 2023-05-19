package subway.dao.entity;

public class SectionWithStationNameEntity {
    private final Long sectionId;
    private final StationEntity upStationEntity;
    private final StationEntity downStationEntity;
    private final int sectionDistance;

    public SectionWithStationNameEntity(final Long sectionId, final StationEntity upStationEntity,
                                        final StationEntity downStationEntity, final int sectionDistance) {
        this.sectionId = sectionId;
        this.upStationEntity = upStationEntity;
        this.downStationEntity = downStationEntity;
        this.sectionDistance = sectionDistance;
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

    public int getSectionDistance() {
        return sectionDistance;
    }
}
