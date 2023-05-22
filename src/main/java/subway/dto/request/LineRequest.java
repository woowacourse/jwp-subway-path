package subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "[ERROR] 노선의 이름을 입력해야 합니다.")
    private String name;
    @NotBlank(message = "[ERROR] 노선의 색상을 입력해야 합니다.")
    private String color;
    @Min(value = 0, message = "[ERROR] 0원 이상의 추가 요금을 입력해야 합니다.")
    private Integer extraFare;

    private LineRequest() {
    }

    public LineRequest(final String name, final String color, final Integer extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
