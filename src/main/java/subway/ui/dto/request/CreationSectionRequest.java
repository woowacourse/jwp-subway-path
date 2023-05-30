package subway.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class CreationSectionRequest {

    @Schema(description = "상행 종점 방향 역 ID")
    @NotNull
    private final Long upStationId;

    @Schema(description = "하행 종점 방향 역 ID")
    @NotNull
    private final Long downStationId;

    @Schema(description = "거리")
    @NotNull
    private final Integer distance;

    private CreationSectionRequest(final Long upStationId, final Long downStationId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static CreationSectionRequest of(final Long upStationId, final Long downStationId, final Integer distance) {
        return new CreationSectionRequest(upStationId, downStationId, distance);
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
}
