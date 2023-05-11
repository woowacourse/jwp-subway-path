package subway.persistence.dao.entity;

public class SectionEntity {
    private final long id;
    private final int distance;
    private final long upStationId;
    private final long downStationId;
    private final int lindId;

    public SectionEntity(long id, int distance, long upStationId, long downStationId, int lindId) {
        this.id = id;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lindId = lindId;
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

    public int getLindId() {
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
