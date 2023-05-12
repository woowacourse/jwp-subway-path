package subway.persistence.dao.entity;

public class PathEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final Integer distance;

    public PathEntity(final Long id, final Long upStationId, final Long downStationId, final Long lineId, final Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
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

    public Integer getDistance() {
        return distance;
    }
}
