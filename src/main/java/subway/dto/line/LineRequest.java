package subway.dto.line;

public class LineRequest {

    private String name;
    private Long lineNumber;
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
