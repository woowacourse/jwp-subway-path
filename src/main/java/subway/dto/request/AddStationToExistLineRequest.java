package subway.dto.request;

public class AddStationToExistLineRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public AddStationToExistLineRequest(Long upStationId, Long downStationId, Integer distance) {
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
