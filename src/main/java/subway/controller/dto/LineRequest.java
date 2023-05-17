package subway.controller.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "노선 이름은 빈 값이 될 수 없습니다.")
    private String name;

    private LineRequest() {
    }

    public LineRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
