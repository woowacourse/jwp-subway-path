package subway.persistence.entity;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Integer distance;
    private final Long previousStationId;
    private final Long nextStationId;

    public SectionEntity(final Long id, final Long lineId, final Integer distance,
                         final Long previousStationId, final Long nextStationId) {
        this.id = id;
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
    }

    public SectionEntity(final Long lineId, final Integer distance,
                         final Long previousStationId, final Long nextStationId) {
        this(null, lineId, distance, previousStationId, nextStationId);
    }

    public static SectionEntity of(final Long id, final SectionEntity sectionEntity) {
        return new SectionEntity(id,
                sectionEntity.lineId, sectionEntity.distance,
                sectionEntity.previousStationId, sectionEntity.nextStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public boolean nextStationIdEqualWithPreviousStationIdFrom(final SectionEntity other) {
        return Objects.equals(nextStationId, other.previousStationId);
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", distance=" + distance +
                ", previousStationId=" + previousStationId +
                ", nextStationId=" + nextStationId +
                '}';
    }
}
