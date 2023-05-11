package subway.dao;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long startStationId;
    private final Long endStationId;
    private final int distance;

    public SectionEntity(Long lineId, Long startStationId, Long endStationId, int distance) {
        this(null, lineId, startStationId, endStationId, distance);
    }

    public SectionEntity(Long id, Long lineId, Long startStationId, Long endStationId,
                         int distance) {
        this.id = id;
        this.lineId = lineId;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public int getDistance() {
        return distance;
    }
}
