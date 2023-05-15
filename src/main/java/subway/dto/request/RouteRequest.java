package subway.dto.request;

import javax.validation.constraints.NotNull;

public class RouteRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long upStation;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long downStation;

    public RouteRequest() {
    }

    public RouteRequest(Long upStation, Long downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getUpStation() {
        return upStation;
    }

    public Long getDownStation() {
        return downStation;
    }
}
