package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {

    @Positive(message = "거리는 양수 값을 입력해야 합니다.")
    private Integer distance;
    @NotNull(message = "상행역은 필수로 입력해야 합니다.")
    private Long upStationId;
    @NotNull(message = "하행역은 필수로 입력해야 합니다.")
    private Long downStationId;
    @NotNull(message = "노선은 필수로 입력해야 합니다.")
    private Long lineId;

    private SectionRequest() {
    }

    public SectionRequest(Integer distance, Long upStationId, Long downStationId, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
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

    public Long getLineId() {
        return lineId;
    }
}
