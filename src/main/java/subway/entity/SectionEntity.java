package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final Integer distance;

    public SectionEntity(Long id, Long sourceStationId, Long targetStationId, Integer distance) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
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

    public Integer getDistance() {
        return distance;
    }
}
