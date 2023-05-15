package subway.application.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class SectionRequest {

    @NotNull(message = "호선 아이디를 입력해 주세요.")
    @Range(min = 1, message = "올바른 호선 아이디를 입력해 주세요.")
    private final Long lineId;

    @NotNull(message = "시작역 아이디를 입력해 주세요.")
    @Range(min = 1, message = "올바른 시작역 아이디를 입력해 주세요.")
    private final Long sourceStationId;

    @NotNull(message = "끝역 아이디를 입력해 주세요.")
    @Range(min = 1, message = "올바른 끝역 아이디를 입력해 주세요.")
    private final Long targetStationId;

    @NotNull(message = "거리를 입력해 주세요.")
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
