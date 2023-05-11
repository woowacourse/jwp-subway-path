package subway.domain;

import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;
    private final String color;

    private Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Line of(long id, String name, String color) {
        return new Line(id, name, color);
    }

    public static Line of(String name, String color) {
        return new Line(null, name, color);
    }

    public boolean isSameName(Line line) {
        return this.name.equals(line.name);
    }

    public boolean isSameColor(Line line) {
        return this.color.equals(line.color);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

}
