package subway.entity;

import java.util.Objects;

public class SectionEntity {

    private Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
        return distance == that.distance && lineId.equals(that.lineId) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, upStationId, downStationId, distance);
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
