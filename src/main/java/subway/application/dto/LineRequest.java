package subway.application.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "호선의 이름을 입력해주세요.")
    private final String name;

    @NotBlank(message = "호선의 색을 입력해주세요.")
    private final String color;

    public LineRequest(final String name, final String color) {
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
