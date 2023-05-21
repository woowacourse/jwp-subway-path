package subway.dao.entity;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private Long startStationId;
    private Long endStationId;
    private double distance;

    public SectionEntity(Long lineId, Long startStationId, Long endStationId, double distance) {
        this(null, lineId, startStationId, endStationId, distance);
    }

    public SectionEntity(Long id, Long lineId, Long startStationId, Long endStationId, double distance) {
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

    public double getDistance() {
        return distance;
    }

    public void updateStartStationId(Long startStationId) {
        this.startStationId = startStationId;
    }

    public void updateEndStationId(Long endStationId) {
        this.endStationId = endStationId;
    }

    public void updateDistance(int distance) {
        this.distance = distance;
    }

}
