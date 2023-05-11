package subway.entity;

public class EdgeEntity {
    private final Long id;
    private final Long lineId;
    private final Long stationId;
    private final Long stationOrder;

    public EdgeEntity(final Long id, final Long lineId, final Long stationId, final Long stationOrder) {
        this.id = id;
        this.lineId = lineId;
        this.stationId = stationId;
        this.stationOrder = stationOrder;
    }

    public EdgeEntity(final Long lineId, final Long stationId, final Long stationOrder) {
        this(null, lineId, stationId, stationOrder);
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

    public Long getStationOrder() {
        return stationOrder;
    }
}
