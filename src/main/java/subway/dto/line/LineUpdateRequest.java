package subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class LineUpdateRequest {
    @NotBlank(message = "노선의 이름은 비어있을 수 없습니다.")
    @Length(
            min = 3,
            max = 15,
            message = "노선의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    private String lineName;

    @NotBlank(message = "노선의 색은 비어있을 수 없습니다.")
    @Pattern(
            regexp = "^bg-[a-z]+-([1-9]00)$",
            message = "노선의 색은 bg-(소문자로 된 색 단어)-(1~9로 시작하는 100 단위의 수) 여야 합니다."
    )
    @Length(
            max = 15,
            message = "노선의 색은 {max}글자를 초과할 수 없습니다."
    )
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
