package subway.dto.request;

import javax.validation.constraints.NotNull;

public class RouteRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long fromStation;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long destStation;

    public RouteRequest() {
    }

    public RouteRequest(Long fromStation, Long destStation) {
        this.fromStation = fromStation;
        this.destStation = destStation;
    }

    public Long getFromStation() {
        return fromStation;
    }

    public Long getDestStation() {
        return destStation;
    }
}
