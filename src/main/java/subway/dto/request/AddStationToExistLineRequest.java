package subway.dto.request;

import subway.dto.AddStationToExistLineDto;

public class AddStationToExistLineRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public AddStationToExistLineRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public AddStationToExistLineDto toDto(Long lineId) {
        return new AddStationToExistLineDto(lineId, upStationId, downStationId, distance);
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
