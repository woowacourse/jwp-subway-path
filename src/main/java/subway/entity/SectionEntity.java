package subway.entity;

public class SectionEntity {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Long lineId;
    private Long distance;

    public SectionEntity(final Long id, final Long upStationId, final Long downStationId, final Long lineId, final Long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public SectionEntity(final Long upStationId, final Long downStationId, final Long lineId, final Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", lineId=" + lineId +
                ", distance=" + distance +
                '}';
    }
}
