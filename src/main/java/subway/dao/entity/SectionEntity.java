package subway.dao.entity;

public class SectionEntity {
    private final Long id;
    private final Long startStationId;
    private final Long endStationId;
    private final Long lineId;
    private final int distance;

    public SectionEntity(final Long id, final Long startStationId, final Long endStationId, final Long lineId,
                         final int distance) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public SectionEntity(final Long startStationId, final Long endStationId, final Long lineId, final int distance) {
        this(null, startStationId, endStationId, lineId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance;
    }
}
