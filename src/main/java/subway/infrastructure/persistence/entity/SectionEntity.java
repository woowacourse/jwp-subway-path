package subway.infrastructure.persistence.entity;

public class SectionEntity {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private Long lineId;

    public SectionEntity(final Long id,
                         final Long upStationId,
                         final Long downStationId,
                         final int distance,
                         final Long lineId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
