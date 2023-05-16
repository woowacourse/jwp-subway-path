package subway.application.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "호선 이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "호선 색상을 입력해 주세요.")
    private String color;

    private Integer extraFare = 0;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
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
