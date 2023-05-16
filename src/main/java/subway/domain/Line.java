package subway.domain;

import java.util.Objects;

public class Line {
    private final String name;
    private final String color;
    private final Sections sections;

    public Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }

        Line line = (Line) o;

        if (!Objects.equals(name, line.name)) {
            return false;
        }
        return Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
