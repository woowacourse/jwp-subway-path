package subway.dto.line;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {
    @NotBlank(message = "노선의 이름은 비어있을 수 없습니다.")
    private String lineName;

    @NotBlank(message = "노선의 색은 비어있을 수 없습니다.")
    private String color;

    public LineUpdateRequest(String lineName, String color) {
        this.lineName = lineName;
        this.color = color;
    }

    public String getLineName() {
        return lineName;
    }

    public String getColor() {
        return color;
    }
}
