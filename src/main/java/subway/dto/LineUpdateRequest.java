package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {

    @NotBlank(message = "노선명을 입력해주세요.")
    private String name;

    @NotBlank(message = "노선 색을 입력해주세요.")
    private String color;

    private LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
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
