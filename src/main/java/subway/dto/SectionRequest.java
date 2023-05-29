package subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotBlank(message = "역 이름을 입력해야 합니다")
    private String upStation;
    @NotBlank(message = "역 이름을 입력해야 합니다")
    private String downStation;
    @NotNull(message = "거리를 입력해야 합니다")
    @Min(value = 1, message = "1이상의 값을 입력해야 합니다")
    private Long distance;

    public SectionRequest() {
    }

    public SectionRequest(final String upStation, final String downStation, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
