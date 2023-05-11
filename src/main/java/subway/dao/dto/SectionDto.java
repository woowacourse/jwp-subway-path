package subway.dao.dto;

public class SectionDto {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final Integer distance;
    private final Boolean isStart;

    public SectionDto(final Long id, final Long upStationId, final Long downStationId, final Long lineId, final Integer distance, final Boolean isStart) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
        this.isStart = isStart;
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

    public Integer getDistance() {
        return distance;
    }

    public Boolean getStart() {
        return isStart;
    }
}
