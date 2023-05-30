package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LineRequest {

    @NotNull(message = "노선명을 입력해주세요.")
    @Size(max = 10, message = "노선명은 10자 이하로 입력해주세요.")
    private String name;

    @NotNull(message = "노선색을 입력해주세요.")
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
