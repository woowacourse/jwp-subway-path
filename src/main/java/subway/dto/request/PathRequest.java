package subway.dto.request;

import javax.validation.constraints.Positive;

public class PathRequest {

    @Positive(message = "지하철 ID는 양수여야합니다.")
    private Long sourceStationId;
    @Positive(message = "지하철 ID는 양수여야합니다.")
    private Long targetStationId;

    public PathRequest() {
    }

    public PathRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
