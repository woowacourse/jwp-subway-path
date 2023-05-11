package subway.entity;

import subway.domain.section.Distance;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final Distance distance;

    public SectionEntity(Long id, Long upStationId,
                         Long downStationId, Long lineId,
                         int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId) && Objects.equals(lineId, that.lineId) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationId, downStationId, lineId, distance);
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", lineId=" + lineId +
                ", distance=" + distance +
                '}';
    }
}
