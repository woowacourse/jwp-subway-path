package subway.application.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class RouteRequest {

    @NotNull(message = "시작역 아이디를 입력해 주세요.")
    @Range(min = 1, message = "올바른 시작역 아이디를 입력해 주세요.")
    private final Long sourceStationId;

    @NotNull(message = "끝역 아이디를 입력해 주세요.")
    @Range(min = 1, message = "올바른 끝역 아이디를 입력해 주세요.")
    private final Long targetStationId;

    public RouteRequest() {
        this(null, null);
    }

    public RouteRequest(final Long sourceStationId, final Long targetStationId) {
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
