package subway.domain;

public class Line {

    private final Long lineNumber;
    private final String name;
    private final String color;

    public Line(final Long lineNumber, final String name, final String color) {
        this.lineNumber = lineNumber;
        this.name = name;
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
