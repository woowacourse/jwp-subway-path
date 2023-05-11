package subway.Entity;

public class SectionEntity {

    private final Long id;
    private final Long upwardId;
    private final Long downwardId;
    private final Integer distance;
    private final long lineId;

    public SectionEntity(Long id, Long upwardId, Long downwardId, Integer distance, long lineId) {
        this.id = id;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public Long getUpwardId() {
        return upwardId;
    }

    public Long getDownwardId() {
        return downwardId;
    }

    public Integer getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }
}
