package subway.persistence.entity.section;

import java.util.Objects;

public class SectionEntity {

    private final Long sectionId;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SectionEntity(final Long sectionId, final Long lineId, final Long upStationId, final Long downStationId, final Long distance) {
        this.sectionId = sectionId;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId, final Long distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SectionEntity)) {
            return false;
        }
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(sectionId, that.sectionId) && Objects.equals(lineId, that.lineId) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId)
                && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId, lineId, upStationId, downStationId, distance);
    }
}
