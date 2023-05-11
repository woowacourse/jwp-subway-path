package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "노선 이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "노선 색깔을 입력해주세요.")
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
