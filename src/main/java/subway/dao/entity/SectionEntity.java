package subway.dao.entity;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private Long startStationId;
    private Long endStationId;
    private int distance;

    public SectionEntity(Long lineId, Long startStationId, Long endStationId, int distance) {
        this(null, lineId, startStationId, endStationId, distance);
    }

    public SectionEntity(Long id, Long lineId, Long startStationId, Long endStationId, int distance) {
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

    public void updateStartStationId(Long startStationId) {
        this.startStationId = startStationId;
    }

    public void updateEndStationId(Long endStationId) {
        this.endStationId = endStationId;
    }

    public void updateDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", startStationId=" + startStationId +
                ", endStationId=" + endStationId +
                ", distance=" + distance +
                '}';
    }
}
