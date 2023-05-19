package subway.dto.request;

import javax.validation.constraints.NotNull;

public class RouteRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long sourceStation;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long targetStation;

    public RouteRequest() {
    }

    public RouteRequest(Long sourceStation, Long targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getSourceStation() {
        return sourceStation;
    }

    public Long getTargetStation() {
        return targetStation;
    }
}
