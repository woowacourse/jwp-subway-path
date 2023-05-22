package subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "노선의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "노선의 번호를 입력해주세요.")
    private Long lineNumber;

    @NotBlank(message = "노선의 색상을 입력해주세요.")
    private String color;

    public LineRequest() {
    }

    public LineRequest(final String name, final Long lineNumber, final String color) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.color = color;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
