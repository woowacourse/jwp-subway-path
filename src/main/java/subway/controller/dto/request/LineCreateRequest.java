package subway.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class LineCreateRequest {

    @Schema(description = "노선 이름")
    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @Schema(description = "노선 색")
    @NotBlank(message = "노선 색깔은 공백일 수 없습니다.")
    private String color;

    public LineCreateRequest() {

    }

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
