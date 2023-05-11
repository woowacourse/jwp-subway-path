package subway.domain;

public class Line {

    private final Sections sections;
    private final Long lineNumber;
    private final String name;
    private final String color;

    public Line(final Sections sections, final Long lineNumber, final String name, final String color) {
        this.sections = sections;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void addSection(final Section section) {
        this.sections.addSection(section);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
