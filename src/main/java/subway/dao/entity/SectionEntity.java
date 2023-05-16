package subway.dao.entity;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long leftStationId;
    private final Long rightStationId;
    private final Integer distance;

    public SectionEntity(Long id, Long lineId, Long leftStationId, Long rightStationId, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
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
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
