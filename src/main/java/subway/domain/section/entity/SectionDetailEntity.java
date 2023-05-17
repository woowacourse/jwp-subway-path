package subway.domain.section.entity;

import subway.domain.station.entity.StationEntity;

import java.util.Objects;

public class SectionDetailEntity {

    private Long id;
    private Long lineId;
    private StationEntity upStationEntity;
    private StationEntity downStationEntity;
    private int distance;

    public SectionDetailEntity(final Long id, final Long lineId, final StationEntity upStationEntity, final StationEntity downStationEntity, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationEntity = upStationEntity;
        this.downStationEntity = downStationEntity;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public StationEntity getUpStation() {
        return upStationEntity;
    }

    public StationEntity getDownStation() {
        return downStationEntity;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionDetailEntity sectionDetailEntity = (SectionDetailEntity) o;
        return Objects.equals(id, sectionDetailEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
