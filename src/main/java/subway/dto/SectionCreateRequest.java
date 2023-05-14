package subway.dto;

public class SectionCreateRequest {

    private final Long lineId;
    private final Long baseStationId;
    private final Long addedStationId;
    private final Boolean direction;
    private final Integer distance;

    public SectionCreateRequest(
            final Long lineId,
            final Long baseStationId,
            final Long addedStationId,
            final Boolean direction,
            final Integer distance
    ) {
        this.lineId = lineId;
        this.baseStationId = baseStationId;
        this.addedStationId = addedStationId;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getBaseStationId() {
        return baseStationId;
    }

    public Long getAddedStationId() {
        return addedStationId;
    }

    public Boolean getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
