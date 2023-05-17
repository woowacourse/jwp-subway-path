package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 공백일 수 없습니다.")
    private String color;

    @NotNull
    @Positive
    private Integer extraFare;

    private LineCreateRequest() {

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
