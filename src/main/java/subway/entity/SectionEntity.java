package subway.entity;

public class SectionEntity {

    private Long id;
    private final Long lineId;
    private final Integer distance;
    private final Long previousStationId;
    private final Long nextStationId;

    public SectionEntity(Long lineId,
                         Integer distance,
                         Long previousStationId,
                         Long nextStationId) {
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
    }

    public SectionEntity(Long id,
                         Long lineId,
                         Integer distance,
                         Long previousStationId,
                         Long nextStationId) {
        this.id = id;
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", distance=" + distance +
                ", previousStationId=" + previousStationId +
                ", nextStationId=" + nextStationId +
                '}';
    }

}
