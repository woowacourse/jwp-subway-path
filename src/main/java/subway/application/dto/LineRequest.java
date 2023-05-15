package subway.application.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "호선 이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "호선 색상을 입력해 주세요.")
    private String color;

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
