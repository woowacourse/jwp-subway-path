package subway.dao.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final Integer distance;

    public SectionEntity(
            final Long lineId,
            final Long sourceStationId,
            final Long targetStationId,
            final Integer distance
    ) {
        this(null, lineId, sourceStationId, targetStationId, distance);
    }

    public SectionEntity(
            final Long id,
            final Long lineId,
            final Long sourceStationId,
            final Long targetStationId,
            final Integer distance
    ) {
        this.id = id;
        this.lineId = lineId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
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

    public Integer getDistance() {
        return distance;
    }
}
