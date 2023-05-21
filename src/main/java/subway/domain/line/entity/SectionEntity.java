package subway.domain.line.entity;


import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionEntity(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        validate(distance);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(final Long id, final SectionEntity sectionEntity) {
        this(id, sectionEntity.lineId, sectionEntity.upStationId, sectionEntity.downStationId, sectionEntity.distance);
    }

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    private void validate(final int distance) {
        if (distance > 0) {
            return;
        }
        throw new IllegalArgumentException("거리는 양의 정수만 가능합니다.");
    }

    public SectionEntity setUpStationId(Long stationId) {
        return new SectionEntity(id, lineId, stationId, downStationId, distance);
    }

    public SectionEntity setDownStationId(Long stationId) {
        return new SectionEntity(id, lineId, upStationId, stationId, distance);
    }

    public SectionEntity setDistance(int distance) {
        return new SectionEntity(id, lineId, upStationId, downStationId, distance);
    }

    public SectionEntity reverseDirection() {
        return new SectionEntity(id, lineId, downStationId, upStationId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }
}
