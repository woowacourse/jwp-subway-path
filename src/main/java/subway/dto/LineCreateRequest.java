package subway.dto;

import javax.validation.constraints.NotNull;

public class LineCreateRequest {

    @NotNull(message = "노선 이름을 입력해 주세요. 입력값 : ${validatedValue}")
    private final String name;

    @NotNull(message = "노선의 색을 입력해 주세요. 입력값: ${validatedValue}")
    private final String color;

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
