package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final Long lineId;
    private final Integer distance;

    public SectionEntity(Long sourceStationId, Long targetStationId, Long lineId, Integer distance) {
        this(null, sourceStationId, targetStationId, lineId, distance);
    }

    public SectionEntity(Long id, Long sourceStationId, Long targetStationId, Long lineId, Integer distance) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
