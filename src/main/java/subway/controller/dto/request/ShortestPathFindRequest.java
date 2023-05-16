package subway.controller.dto.request;

import javax.validation.constraints.NotNull;

public class ShortestPathFindRequest {

    @NotNull(message = "출발역 ID는 존재해야 합니다.")
    private Long startStationId;

    @NotNull(message = "도착역 ID는 존재해야 합니다.")
    private Long endStationId;

    private ShortestPathFindRequest() {
    }

    public ShortestPathFindRequest(final Long startStationId, final Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
