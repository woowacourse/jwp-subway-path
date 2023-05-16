package subway.dao.entity;

import java.util.Objects;

public class SectionEntity {

    private final Long lineId;
    private final Long leftStationId;
    private final Long rightStationId;
    private final Integer distance;

    public SectionEntity(Long lineId, Long leftStationId, Long rightStationId, Integer distance) {
        this.lineId = lineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
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
        return Objects.equals(getLineId(), that.getLineId()) && Objects.equals(getLeftStationId(),
                that.getLeftStationId()) && Objects.equals(getRightStationId(), that.getRightStationId())
                && Objects.equals(getDistance(), that.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineId(), getLeftStationId(), getRightStationId(), getDistance());
    }
}
