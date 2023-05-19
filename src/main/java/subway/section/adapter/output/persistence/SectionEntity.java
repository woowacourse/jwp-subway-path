package subway.section.adapter.output.persistence;

import java.util.Objects;

public class SectionEntity {
    private final Long id;
    private final Long firstStationId;
    private final Long secondStationId;
    private final Long distance;
    private final Long lineId;
    
    public SectionEntity(final Long firstStationId, final Long secondStationId, final Long distance, final Long lineId) {
        this(null, firstStationId, secondStationId, distance, lineId);
    }
    
    public SectionEntity(final Long id, final Long firstStationId, final Long secondStationId, final Long distance, final Long lineId) {
        this.id = id;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.distance = distance;
        this.lineId = lineId;
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getFirstStationId() {
        return firstStationId;
    }
    
    public Long getSecondStationId() {
        return secondStationId;
    }
    
    public Long getDistance() {
        return distance;
    }
    
    public Long getLineId() {
        return lineId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstStationId, that.firstStationId) && Objects.equals(secondStationId, that.secondStationId) && Objects.equals(distance, that.distance) && Objects.equals(lineId, that.lineId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, firstStationId, secondStationId, distance, lineId);
    }
    
    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", firstStationId=" + firstStationId +
                ", secondStationId=" + secondStationId +
                ", distance=" + distance +
                ", lineId=" + lineId +
                '}';
    }
}
