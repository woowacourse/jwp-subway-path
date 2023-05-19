package subway.dto;

public class AddOneSectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    private AddOneSectionRequest() {
        this.distance = null;
        this.upStationId = null;
        this.downStationId = null;
    }

    public AddOneSectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
