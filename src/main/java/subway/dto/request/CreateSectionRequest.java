package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateSectionRequest {

    @NotNull
    private Long upStation;

    @NotNull
    private Long downStation;

    @Positive(message = "두 역간 거리는 양의 정수여야합니다.")
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
