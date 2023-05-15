package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "라인 이름은 공백일 수 없습니다.")
    private String name;
    @NotBlank(message = "색 이름은 공백일 수 없습니다.")
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
