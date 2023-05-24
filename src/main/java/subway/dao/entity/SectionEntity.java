package subway.dao.entity;

public class SectionEntity {

    private final Long id;
    private final Integer distance;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;

    public SectionEntity(int distance, Long upStationId, Long downStationId, Long lineId) {
        this(null, distance, upStationId, downStationId, lineId);
    }

    public SectionEntity(Long id, Integer distance, Long upStationId, Long downStationId, Long lineId) {
        this.id = id;
        this.distance = distance;
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
