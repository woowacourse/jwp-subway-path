package subway.ui.dto.line;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {

    @NotBlank(message = "노선의 이름은 비어있을 수 없습니다.")
    private String name;

    @NotBlank(message = "노선의 색은 비어있을 수 없습니다.")
    private String color;

    public LineUpdateRequest() {
    }

    @JsonCreator
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
