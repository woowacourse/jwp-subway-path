package subway.persistence.entity;

public class LineStationEntity {

    private final Long id;
    private final Long lineId;
    private final Long stationId;

    private LineStationEntity(final Long id, final Long lineId, final Long stationId) {
        this.id = id;
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public static LineStationEntity of(final Long lineId, final Long stationId) {
        return new LineStationEntity(null, lineId, stationId);
    }

    public static LineStationEntity of(final Long id, final Long lineId, final Long stationId) {
        return new LineStationEntity(id, lineId, stationId);
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
}
