package subway.persistence.entity;

import java.util.Objects;

public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final String upwardStation;
    private final String downwardStation;
    private final Integer distance;

    public SectionEntity(Long lineId, String upwardStation, String downwardStation, Integer distance) {
        this(null, lineId, upwardStation, downwardStation, distance);
    }

    public SectionEntity(Long id, Long lineId, String upwardStation, String downwardStation, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getUpwardStation() {
        return upwardStation;
    }

    public String getDownwardStation() {
        return downwardStation;
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
