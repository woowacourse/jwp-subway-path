package subway.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(
        description = "노선 생성 요청 정보",
        example = "{\"name\": \"2호선\", \"color\": \"초록색\", \"fare\": 1000}"
)
public class LineCreateRequest {

    @Schema(description = "노선 이름")
    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @Schema(description = "노선 색")
    @NotBlank(message = "노선 색깔은 공백일 수 없습니다.")
    private String color;

    @Schema(description = "노선 추가 요금")
    @NotNull(message = "노선 추가 요금은 존재해야 합니다.")
    @Min(value = 0, message = "노선 추가 요금은 0원 이상 가능합니다.")
    private Integer fare;

    private LineCreateRequest() {
    }

    public LineCreateRequest(final String name, final String color, final Integer fare) {
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getFare() {
        return fare;
    }
}
