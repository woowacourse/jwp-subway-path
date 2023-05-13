package subway.dto.request;

import javax.validation.constraints.Positive;

public class SectionRequest {
    @Positive(message = "지하철 ID는 양수여야합니다.")
    private Long upBoundStationId;
    @Positive(message = "지하철 ID는 양수여야합니다.")
    private Long downBoundStationId;
    @Positive(message = "거리는 0이하일 수 없습니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upBoundStationId, Long downBoundStationId, int distance) {
        this.upBoundStationId = upBoundStationId;
        this.downBoundStationId = downBoundStationId;
        this.distance = distance;
    }

    public Long getUpBoundStationId() {
        return upBoundStationId;
    }

    public Long getDownBoundStationId() {
        return downBoundStationId;
    }

    public int getDistance() {
        return distance;
    }
}
