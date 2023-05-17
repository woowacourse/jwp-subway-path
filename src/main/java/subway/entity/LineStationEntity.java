package subway.entity;

public class LineStationEntity {

    private final Long id;
    private final Long stationId;
    private final Long lineId;

    public LineStationEntity(final Long id, final Long stationId, final Long lineId) {
        this.id = id;
        this.stationId = stationId;
        this.lineId = lineId;
    }

    public LineStationEntity(final Long stationId, final Long lineId) {
        this(null, stationId, lineId);
    }

    public Long getId() {
        return id;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getLineId() {
        return lineId;
    }
}
