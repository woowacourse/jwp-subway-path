package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upwardStationId;
    private final String upwardStation;
    private final Long downwardStationId;
    private final String downwardStation;
    private final Integer distance;

    public SectionEntity(final Long id, final Long lineId, final Long upwardStationId, final String upwardStation,
                         final Long downwardStationId,
                         final String downwardStation, final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upwardStationId = upwardStationId;
        this.upwardStation = upwardStation;
        this.downwardStationId = downwardStationId;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public String getUpwardStation() {
        return upwardStation;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public String getDownwardStation() {
        return downwardStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
