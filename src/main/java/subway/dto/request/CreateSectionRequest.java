package subway.dto.request;

import javax.validation.constraints.NotNull;

public class CreateSectionRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long upStation;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long downStation;

    @NotNull(message = "거리는 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Integer distance;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long upStation, final Long downStation, final Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getUpStation() {
        return upStation;
    }

    public Long getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
