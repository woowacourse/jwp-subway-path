package subway.Entity;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long upwardId;
    private final Long downwardId;
    private final Integer distance;
    private final long lineId;

    public SectionEntity(Long id, Long upwardId, Long downwardId, Integer distance, long lineId) {
        this.id = id;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public Long getUpwardId() {
        return upwardId;
    }

    public Long getDownwardId() {
        return downwardId;
    }

    public Integer getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", upwardId=" + upwardId +
                ", downwardId=" + downwardId +
                ", distance=" + distance +
                ", lineId=" + lineId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEntity that = (SectionEntity) o;
        return lineId == that.lineId && Objects.equals(id, that.id) && Objects.equals(upwardId, that.upwardId) && Objects.equals(downwardId, that.downwardId) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upwardId, downwardId, distance, lineId);
    }
}
