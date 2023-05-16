package subway.application.dto;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull(message = "호선의 이름을 입력해주세요.")
    private String name;
    @NotNull(message = "호선의 색을 입력해주세요.")
    private String color;

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
