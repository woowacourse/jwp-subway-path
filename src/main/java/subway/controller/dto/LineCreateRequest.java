package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.lang.Nullable;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 공백일 수 없습니다.")
    private String color;

    @Nullable
    @PositiveOrZero(message = "노선의 추가 요금은 양수여야합니다.")
    private Integer extraFare;

    private LineCreateRequest() {
        this.extraFare = 0;
    }

    public LineCreateRequest(final String name, final String color, final Integer extraFare) {
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
