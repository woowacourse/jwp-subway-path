package subway.entity;

import java.util.Objects;
import subway.domain.section.Distance;

public class SectionEntity {
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final long lineId;

    private SectionEntity(final long upStationId, final long downStationId, final int distance, final long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static SectionEntity of(long upStationId, long downStationId, Distance distance, long lineId) {
        return new SectionEntity(upStationId, downStationId, distance.getValue(), lineId);
    }

    public static SectionEntity of(long upStationId, long downStationId, int distance, long lineId) {
        return new SectionEntity(upStationId, downStationId, distance, lineId);
    }

    public static SectionEntity of(StationEntity upStationEntity, StationEntity downStationEntity, Distance distance, long lineId) {
        return new SectionEntity(upStationEntity.getId(), downStationEntity.getId(), distance.getValue(), lineId);
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
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
        return upStationId == that.upStationId && downStationId == that.downStationId && distance == that.distance
                && lineId == that.lineId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId, distance, lineId);
    }
}
