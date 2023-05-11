package subway.dto;

import javax.validation.constraints.NotBlank;

public class SectionRequest {

    @NotBlank(message = "호선 식별자가 입력되지 않았습니다.")
    private final Long lineId;

    @NotBlank(message = "출발지 식별자가 입력되지 않았습니다.")
    private final Long sourceStationId;

    @NotBlank(message = "도착지 식별자가 입력되지 않았습니다.")
    private final Long targetStationId;

    @NotBlank(message = "거리가 입력되지 않았습니다.")
    private final Integer distance;

    public SectionRequest() {
        this(null, null, null, null);
    }

    public SectionRequest(final Long lineId, final Long sourceStationId,
                          final Long targetStationId, final Integer distance) {
        this.lineId = lineId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
