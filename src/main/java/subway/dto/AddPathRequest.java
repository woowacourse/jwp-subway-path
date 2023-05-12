package subway.dto;

public class AddPathRequest {

    private final Long targetStationId;
    private final Long addStationId;
    private final Integer distance;
    private final String direction;

    public AddPathRequest(final Long targetStationId, final Long addStationId, final Integer distance, final String direction) {
        this.targetStationId = targetStationId;
        this.addStationId = addStationId;
        this.distance = distance;
        this.direction = direction;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getAddStationId() {
        return addStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getDirection() {
        return direction;
    }
}
