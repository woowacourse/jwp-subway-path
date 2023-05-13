package subway.section.dto;

import subway.station.persistence.StationEntity;

public class SectionStationDto {

    private final Long sectionId;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final int distance;

    public SectionStationDto(
        final Long sectionId,
        final Long upStationId,
        final String upStationName,
        final Long downStationId,
        final String downStationName,
        final int distance
    ) {
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    public StationEntity getUpStationEntity() {
        return new StationEntity(upStationId, upStationName);
    }

    public StationEntity getDownStationEntity() {
        return new StationEntity(downStationId, downStationName);
    }
}
