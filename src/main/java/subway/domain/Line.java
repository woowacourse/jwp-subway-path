package subway.domain;

public class Line {

    private final Sections sections;
    private final String name;
    private final String color;

    public Line(final Sections sections, final String name, final String color) {
        this.sections = sections;
        this.name = name;
        this.color = color;
    }

    public Sections getSections() {
        return sections;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
