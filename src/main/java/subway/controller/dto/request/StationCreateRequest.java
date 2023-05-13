package subway.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @Schema(description = "역 이름")
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String name;

    public StationCreateRequest() {

    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
