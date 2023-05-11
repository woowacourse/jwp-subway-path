package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {

    @NotNull(message = "호선 ID는 null이 될 수 없습니다.")
    private final Long lineId;

    @NotNull(message = "상행역 ID는 null이 될 수 없습니다.")
    private final Long upStationId;

    @NotNull(message = "하행역 ID는 null이 될 수 없습니다.")
    private final Long stationId;

    @NotNull(message = "거리는 null이 될 수 없습니다.")
    @Positive(message = "거리는 양의 정수만 가능합니다.")
    private final int distance;

    public SectionRequest(final Long lineId, final Long upStationId, final Long stationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.stationId = stationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }
}
