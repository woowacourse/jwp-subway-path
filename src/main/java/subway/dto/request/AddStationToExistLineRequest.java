package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import subway.dto.AddStationToExistLineDto;

public class AddStationToExistLineRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다.")
    private final Long upStationId;

    @NotNull(message = "하행역은 비어있을 수 없습니다.")
    private final Long downStationId;

    @NotNull(message = "역 간 거리는 비어있을 수 없습니다.")
    @Positive(message = "역 간 거리는 양수여야 합니다.")
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
