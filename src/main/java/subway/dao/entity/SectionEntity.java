package subway.dao.entity;

public class SectionEntity {

    private final Long id;
    private final Integer distance;
    private final Boolean isStart;
    private final Long upStationId;
    private final Long downStationId;

    public SectionEntity(final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId) {
        this(null, distance, isStart, upStationId, downStationId);
    }

    public SectionEntity(final Long id, final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId) {
        this.id = id;
        this.distance = distance;
        this.isStart = isStart;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public Integer getDistance() {
        return distance;
    }

    public Boolean getStart() {
        return isStart;
    }
}
