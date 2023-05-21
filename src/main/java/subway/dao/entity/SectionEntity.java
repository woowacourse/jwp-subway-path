package subway.dao.entity;

public class SectionEntity {
    private Long id;
    private long lineId;
    private long startStationId;
    private long endStationId;
    private double distance;

    public SectionEntity(long lineId, long startStationId, long endStationId, double distance) {
        this(null, lineId, startStationId, endStationId, distance);
    }

    public SectionEntity(Long id, long lineId, long startStationId, long endStationId, double distance) {
        this.id = id;
        this.lineId = lineId;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStartStationId() {
        return startStationId;
    }

    public long getEndStationId() {
        return endStationId;
    }

    public double getDistance() {
        return distance;
    }

    public void updateStartStationId(long startStationId) {
        this.startStationId = startStationId;
    }

    public void updateEndStationId(long endStationId) {
        this.endStationId = endStationId;
    }

    public void updateDistance(int distance) {
        this.distance = distance;
    }

}
