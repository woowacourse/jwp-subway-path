package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SubwayPathRequest {
    @Schema(description = "출발 역의 ID")
    private final Long sourceStationId;

    @Schema(description = "도착 역의 ID")
    private final Long targetStationId;

    public SubwayPathRequest(Long sourceStationId, Long targetStationId) {
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
