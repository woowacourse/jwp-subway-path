package subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineEditRequest {

    @NotBlank(message = "노선의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "노선의 번호를 입력해주세요.")
    private long lineNumber;

    @NotBlank(message = "노선의 색상을 입력해주세요.")
    private String color;

    private LineEditRequest() {
    }

    public LineEditRequest(final String name, final long lineNumber, final String color) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getColor() {
        return color;
    }
}
