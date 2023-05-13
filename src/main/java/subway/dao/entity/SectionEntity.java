package subway.dao.entity;

public class SectionEntity {

    private final Long id;
    private final Integer distance;
    private final Boolean isStart;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;

    public SectionEntity(final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId, final Long lineId) {
        this(null, distance, isStart, upStationId, downStationId, lineId);
    }

    public SectionEntity(final Long id, final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId, final Long lineId) {
        this.id = id;
        this.distance = distance;
        this.isStart = isStart;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public Boolean getStart() {
        return isStart;
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
}
