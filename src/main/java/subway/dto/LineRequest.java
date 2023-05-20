package subway.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @NotBlank(message = "노선의 이름은 비어있을 수 없습니다.")
    @Length(min = 1, max = 10, message = "노선의 이름은 {min}자 ~ {max}자여야 합니다")
    private String name;

    @NotBlank(message = "노선의 색깔은 비어있을 수 없습니다.")
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
