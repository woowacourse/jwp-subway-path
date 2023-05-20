package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class SectionCreateRequest {

    @NotBlank
    @Size(max = 10, message = "역의 이름은 최대 {max}자 까지만 가능합니다.")
    private String baseStation;
    @NotBlank
    @Size(max = 10, message = "역의 이름은 최대 {max}자 까지만 가능합니다.")
    private String newStation;
    @NotBlank
    @Pattern(regexp = "^(상행|하행)$", message = "입력값은 '상행' 또는 '하행' 이어야 합니다.")
    private String direction;

    @NotNull
    @Range(min = 0, message = "추가요금은 {min} 이상이여야 합니다.")
    private Integer distance;

    SectionCreateRequest() {
    }

    public SectionCreateRequest(final String baseStation, final String newStation, final String direction,
        final Integer distance) {
        this.baseStation = baseStation;
        this.newStation = newStation;
        this.direction = direction;
        this.distance = distance;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getNewStation() {
        return newStation;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
