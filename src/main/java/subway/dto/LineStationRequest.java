package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineStationRequest {

    @NotNull(message = "상행역 정보를 입력해주세요.")
    private final Long upStationId;
    
    @NotNull(message = "하행역 정보를 입력해주세요.")
    private final Long downStationId;

    @NotNull(message = "거리 정보를 입력해주세요.")
    @Positive(message = "거리는 0 이상만 입력해주세요")
    private final Integer distance;

    public LineStationRequest(final Long upStationId, final Long downStationId, final Integer distance) {
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
