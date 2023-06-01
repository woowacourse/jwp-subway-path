package subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "시작역은 필수로 입력해야 합니다.")
    private Long startStationId;
    @NotNull(message = "도착역은 필수로 입력해야 합니다.")
    private Long targetStationId;

    public PathRequest() {
    }

    public PathRequest(Long startStationId, Long targetStationId) {
        this.startStationId = startStationId;
        this.targetStationId = targetStationId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
