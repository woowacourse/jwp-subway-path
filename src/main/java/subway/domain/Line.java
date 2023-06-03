package subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final Long lineNumber;
    private final String name;
    private final String color;
    private final int additionalFare;

    public Line(Long lineNumber, String name, String color) {
        this(null, lineNumber, name, color, 0);
    }

    public Line(Long lineNumber, String name, String color, int additionalFare) {
        this(null, lineNumber, name, color, additionalFare);
    }

    public Line(Long id, Long lineNumber, String name, String color, int additionalFare) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
