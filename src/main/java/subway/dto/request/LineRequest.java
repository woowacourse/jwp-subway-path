package subway.dto.request;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull(message = "라인 이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private String name;

    @NotNull(message = "라인 이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
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
