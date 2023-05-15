package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long startStationId;
    private final Long endStationId;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(
            final Long startStationId,
            final Long endStationId,
            final Integer distance,
            final Long lineId) {
        this(null, startStationId, endStationId, distance, lineId);
    }

    public SectionEntity(
            final Long id,
            final Long startStationId,
            final Long endStationId,
            final Integer distance,
            final Long lineId) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.distance = distance;
        this.lineId = lineId;
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

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
