package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineStationRequest {

    @NotNull(message = "상행역 ID는 null이 될 수 없습니다.")
    private final Long upStationId;

    @NotNull(message = "하행역 ID는 null이 될 수 없습니다.")
    private final Long downStationId;

    @NotNull(message = "거리는 null이 될 수 없습니다.")
    @Positive(message = "거리는 양의 정수만 가능합니다.")
    private final int distance;

    public LineStationRequest(final Long upStationId, final Long downStationId, final int distance) {
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

    public int getDistance() {
        return distance;
    }
}
