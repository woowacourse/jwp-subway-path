package subway.entity;

public class LineStationEntity {
    private final StationEntity upStationEntity;
    private final StationEntity downStationEntity;
    private final SectionEntity sectionEntity;

    public LineStationEntity(final StationEntity upStationEntity, final StationEntity downStationEntity, final SectionEntity sectionEntity) {
        this.upStationEntity = upStationEntity;
        this.downStationEntity = downStationEntity;
        this.sectionEntity = sectionEntity;
    }

    public StationEntity getUpStationEntity() {
        return upStationEntity;
    }

    public StationEntity getDownStationEntity() {
        return downStationEntity;
    }

    public SectionEntity getSectionEntity() {
        return sectionEntity;
    }
}
