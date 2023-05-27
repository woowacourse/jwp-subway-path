package subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final Long lineNumber;
    private final String name;
    private final String color;

    public Line(final Long lineNumber, final String name, final String color) {
        this(null, lineNumber, name, color);
    }

    public Line(final Long id, final Long lineNumber, final String name, final String color) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
