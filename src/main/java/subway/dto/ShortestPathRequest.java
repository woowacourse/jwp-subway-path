package subway.dto;

import javax.validation.constraints.Positive;

public class ShortestPathRequest {
    @Positive(message = "역 ID는 양수여야 합니다.")
    private Long startStationId;
    @Positive(message = "역 ID는 양수여야 합니다.")
    private Long endStationId;

    private ShortestPathRequest() {
    }

    public ShortestPathRequest(final Long startStationId, final Long endStationId) {
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
