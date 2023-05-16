package subway.presentation.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull
    @Length(min = 1, max = 10, message = "역 이름은 1글자 이상, 10글자 이하여야 합니다.")
    private String startStation;

    @NotNull
    @Length(min = 1, max = 10, message = "역 이름은 1글자 이상, 10글자 이하여야 합니다.")
    private String endStation;

    @NotNull
    @Range(min = 1, message = "거리는 양의 정수여야 합니다.")
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(String startStation, String endStation, Integer distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
