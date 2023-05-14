package subway.dto.line;

public class LineResponse {

    private final Long id;
    private final Long lineNumber;
    private final String name;
    private final String color;

    public LineResponse(final Long id, final Long lineNumber, final String name, final String color) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
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
