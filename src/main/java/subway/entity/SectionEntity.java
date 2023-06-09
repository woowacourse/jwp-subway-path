package subway.entity;

public class SectionEntity {

    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private long id;

    public SectionEntity(final long lineId, final long upStationId, final long downStationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(final long id, final long lineId, final long upStationId, final long downStationId,
                         final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getId() {
        return this.id;
    }

    public long getLineId() {
        return this.lineId;
    }

    public long getUpStationId() {
        return this.upStationId;
    }

    public long getDownStationId() {
        return this.downStationId;
    }

    public int getDistance() {
        return this.distance;
    }
}
