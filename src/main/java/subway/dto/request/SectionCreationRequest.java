package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionCreationRequest {

    @NotNull(message = "노선의 id를 입력해야 합니다.")
    private Long lineId;
    @NotNull(message = "상행 방향 역의 id를 입력해야 합니다.")
    private Long upwardStationId;
    @NotNull(message = "하행 방향 역의 id를 입력해야 합니다.")
    private Long downwardStationId;
    @Positive(message = "두 역 사이의 거리는 양의 정수여야 합니다.")
    private Integer distance;

    private SectionCreationRequest() {
    }

    public SectionCreationRequest(final Long lineId, final Long upwardStationId, final Long downwardStationId, final Integer distance) {
        this.lineId = lineId;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
