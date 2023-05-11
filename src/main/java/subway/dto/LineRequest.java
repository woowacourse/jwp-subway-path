package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LineRequest {

    @Size(min = 1, max = 5, message = "호선 이름은 1자 이상 5자 이하만 가능합니다.")
    @NotBlank(message = "호선 이름은 공백을 입력할 수 없습니다.")
    private String name;

    @Size(min = 5, max = 20, message = "호선 색깔은 5자 이상 20자 이하만 가능합니다.")
    @NotBlank(message = "호선 색깔은 공백을 입력할 수 없습니다.")
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
