package subway.dto;

import subway.dto.request.AddStationToExistLineRequest;

public class AddStationToExistLineDto {
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    private AddStationToExistLineDto(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static AddStationToExistLineDto from(Long lineId, AddStationToExistLineRequest request) {
        return new AddStationToExistLineDto(
                lineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
    }

    public Long getLineId() {
        return lineId;
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
