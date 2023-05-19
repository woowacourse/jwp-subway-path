package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    private LineRequest() {
    }

    public LineRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
