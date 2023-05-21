package subway.domain.line;

import java.util.Objects;

public class Line {
    private final LineName name;
    private final LineColor color;

    public Line(LineName name, LineColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
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
