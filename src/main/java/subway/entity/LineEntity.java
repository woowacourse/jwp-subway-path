package subway.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final Long stationId;
    private final Long stationOrder;

    public LineEntity(final Long id, final String name, final Long stationId, final Long stationOrder) {
        this.id = id;
        this.name = name;
        this.stationId = stationId;
        this.stationOrder = stationOrder;
    }

    public LineEntity(final String name, final Long stationId, final Long stationOrder) {
        this(null, name, stationId, stationOrder);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getStationOrder() {
        return stationOrder;
    }
}
