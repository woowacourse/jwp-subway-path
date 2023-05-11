package subway.entity;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private Long sourceStationId;
    private Long targetStationId;
    private Long distance;

    public SectionEntity(Long id, Long lineId, Long sourceStationId, Long targetStationId, Long distance) {
        this.id = id;
        this.lineId = lineId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
    }

    public SectionEntity(Long lineId, Long sourceStationId, Long targetStationId, Long distance) {
        this(null, lineId, sourceStationId, targetStationId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
