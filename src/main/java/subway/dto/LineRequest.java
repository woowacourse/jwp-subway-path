package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String name;
    @NotBlank(message = "노선 색을 입력해야 합니다")
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
