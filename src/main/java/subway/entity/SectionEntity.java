package subway.entity;

import java.util.Objects;
import subway.domain.section.Section;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upwardStationId;
    private final String upwardStation;
    private final Long downwardStationId;
    private final String downwardStation;
    private final Integer distance;

    public SectionEntity(
            final Long lineId,
            final Long upwardStationId,
            final Long downwardStationId,
            final Integer distance
    ) {
        this(null, lineId, upwardStationId, null, downwardStationId, null, distance);
    }

    public SectionEntity(
            final Long id, final Long lineId, final Long upwardStationId, final String upwardStation,
            final Long downwardStationId, final String downwardStation, final Integer distance
    ) {
        this.id = id;
        this.lineId = lineId;
        this.upwardStationId = upwardStationId;
        this.upwardStation = upwardStation;
        this.downwardStationId = downwardStationId;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static SectionEntity of(final Long lineId, final Section section) {
        return new SectionEntity(
                lineId,
                section.getUpward().getId(),
                section.getDownward().getId(),
                section.getDistance()
        );
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

    public String getUpwardStation() {
        return upwardStation;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public String getDownwardStation() {
        return downwardStation;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
