package subway.dao.entity;

import java.util.Objects;

public class SectionEntity {
    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final int distance;

    public SectionEntity(final Long id, final Long upStationId, final Long downStationId, final Long lineId,
                         final int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public SectionEntity(final Long upStationId, final Long downStationId, final Long lineId, final int distance) {
        this(null, upStationId, downStationId, lineId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
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
        SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}