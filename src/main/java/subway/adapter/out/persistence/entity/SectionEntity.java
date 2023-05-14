package subway.adapter.out.persistence.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId,
            final Integer distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public SectionEntity(final Long id, final Long lineId, final Long upStationId, final Long downStationId,
            final Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
