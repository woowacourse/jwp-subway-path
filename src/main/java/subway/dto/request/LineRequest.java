package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "호선명은 빈 문자열일 수 없습니다.")
    private final String name;

    @NotBlank(message = "호선의 색상은 빈 문자열일 수 없습니다.")
    private final String color;

    public LineRequest(final String name, final String color) {
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
