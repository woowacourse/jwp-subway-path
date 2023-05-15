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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SectionEntity)) return false;
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(lineId, that.lineId) && Objects.equals(distance, that.distance) && Objects.equals(previousStationId, that.previousStationId) && Objects.equals(nextStationId, that.nextStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, distance, previousStationId, nextStationId);
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
