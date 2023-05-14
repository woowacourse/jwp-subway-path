package subway.entity;

import java.util.Objects;

public class EdgeEntity {
    private final Long id;
    private final Long lineId;
    private final Long stationId;
    private final int stationOrder;
    private final Integer distanceToNext;

    public EdgeEntity(Long id, Long lineId, Long stationId, int stationOrder, Integer distanceToNext) {
        this.id = id;
        this.lineId = lineId;
        this.stationId = stationId;
        this.stationOrder = stationOrder;
        this.distanceToNext = distanceToNext;
    }

    public EdgeEntity(final Long lineId, final Long stationId, final int stationOrder, final Integer distanceToNext) {
        this(null, lineId, stationId, stationOrder, distanceToNext);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeEntity that = (EdgeEntity) o;
        return distanceToNext == that.distanceToNext && Objects.equals(id, that.id) && Objects.equals(lineId, that.lineId) && Objects.equals(stationId, that.stationId) && Objects.equals(stationOrder, that.stationOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, stationId, stationOrder, distanceToNext);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getStationOrder() {
        return stationOrder;
    }

    public Integer getDistanceToNext() {
        return distanceToNext;
    }
}
