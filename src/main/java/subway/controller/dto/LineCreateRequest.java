package subway.controller.dto;

import javax.validation.constraints.NotBlank;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 공백일 수 없습니다.")
    private String color;

    private LineCreateRequest() {

    }

    public LineCreateRequest(final String name, final String color) {
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
