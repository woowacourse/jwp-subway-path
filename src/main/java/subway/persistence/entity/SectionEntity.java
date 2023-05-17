package subway.persistence.entity;

public class SectionEntity {

    private final Long id;
    private final Long beforeStation;
    private final Long nextStation;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(final Long id, final Long beforeStation, final Long nextStation, final Integer distance, final Long lineId) {
        this.id = id;
        this.beforeStation = beforeStation;
        this.nextStation = nextStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public SectionEntity(final Long beforeStation, final Long nextStation, final Integer distance, final Long lineId) {
        this(null, beforeStation, nextStation, distance, lineId);
    }

    public Long getId() {
        return id;
    }

    public Long getBeforeStation() {
        return beforeStation;
    }

    public Long getNextStation() {
        return nextStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
