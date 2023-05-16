package subway.dto;

public class AddTwoSectionRequest {

    private final Long newStationId;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer upStationDistance;
    private final Integer downStationDistance;

    private AddTwoSectionRequest() {
        this(null, null, null, null, null);
    }

    public AddTwoSectionRequest(final Long newStationId, final Long upStationId, final Long downStationId, final Integer upStationDistance, final Integer downStationDistance) {
        this.newStationId = newStationId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.upStationDistance = upStationDistance;
        this.downStationDistance = downStationDistance;
    }

    public Long getNewStationId() {
        return newStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getUpStationDistance() {
        return upStationDistance;
    }

    public Integer getDownStationDistance() {
        return downStationDistance;
    }
}
