package subway.persistence.dao.entity;

import java.util.Objects;

public class SectionEntity {
    private final Long id;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final long lindId;

    public SectionEntity(Long id, long upStationId, long downStationId, int distance, long lindId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lindId = lindId;
    }

    public SectionEntity(long upStationId, long downStationId, int distance, long lindId) {
        this(null, upStationId, downStationId, distance, lindId);
    }

    public long getId() {
        if (id == null) {
            throw new IllegalStateException("현재 id값이 존재 하지않습니다.");
        }
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getLindId() {
        return lindId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEntity that = (SectionEntity) o;
        return upStationId == that.upStationId && downStationId == that.downStationId && distance == that.distance && lindId == that.lindId && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationId, downStationId, distance, lindId);
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", distance=" + distance +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", lindId=" + lindId +
                '}';
    }
}
