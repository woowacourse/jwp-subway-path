package subway.entity;

import subway.domain.section.Distance;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;
    private final Long lineId;

    public SectionEntity(Long id, Long upStationId, Long downStationId, int distance, Long lineId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = new Distance(distance);
        this.lineId = lineId;
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

    public int getDistance() {
        return distance.getDistance();
    }

    public Long getLineId() {
        return lineId;
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
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", lineId=" + lineId +
                '}';
    }
}
