package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "노선 이름은 빈 값이 될 수 없습니다.")
    private String name;

    @Positive(message = "추가 금액은 0원 이상부터 가능합니다.")
    private Integer extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, Integer extraFare) {
        this.name = name;
        this.extraFare = extraFare;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public String getName() {
        return name;
    }
}
