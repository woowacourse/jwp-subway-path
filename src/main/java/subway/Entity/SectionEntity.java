package subway.Entity;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final long lineId;
    private final long upwardId;
    private final long downwardId;
    private final int distance;

    public SectionEntity(Long id, long lineId, long upwardId, long downwardId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public long getUpwardId() {
        return upwardId;
    }

    public long getDownwardId() {
        return downwardId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEntity that = (SectionEntity) o;
        return lineId == that.lineId
                && Objects.equals(id, that.id)
                && Objects.equals(upwardId, that.upwardId)
                && Objects.equals(downwardId, that.downwardId)
                && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upwardId, downwardId, distance, lineId);
    }
}
