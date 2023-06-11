package subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class LineCreateRequest {

    @NotBlank(message = "노선의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "노선의 번호를 입력해주세요.")
    private Long lineNumber;

    @NotBlank(message = "노선의 색상을 입력해주세요.")
    private String color;

    @PositiveOrZero(message = "노선 추가 운임은 음수일 수 없습니다.")
    private int additionalFare;

    public LineCreateRequest() {
    }

    public LineCreateRequest(final String name, final Long lineNumber, final String color) {
        this(name, lineNumber, color, 0);
    }

    public LineCreateRequest(String name, Long lineNumber, String color, int additionalFare) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public String getName() {
        return name;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getColor() {
        return color;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
