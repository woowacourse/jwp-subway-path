package subway.persistence.entity;

import java.util.Objects;

public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final Long upwardStationId;
    private final Long downwardStationId;
    private final Integer distance;

    public SectionEntity(Long lineId, Long upwardStationId, Long downwardStationId, Integer distance) {
        this(null, lineId, upwardStationId, downwardStationId, distance);
    }

    public SectionEntity(Long id, Long lineId, Long upwardStationId, Long downwardStationId, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
