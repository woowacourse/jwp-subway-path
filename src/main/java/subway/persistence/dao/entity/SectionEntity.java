package subway.persistence.dao.entity;

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
