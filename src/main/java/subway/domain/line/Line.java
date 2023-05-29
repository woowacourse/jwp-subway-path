package subway.domain.line;

import java.util.Objects;
import subway.domain.Name;

public class Line {
    private final Long id;
    private final Name name;
    private final Color color;

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name =  new Name(name);
        this.color = new Color(color);
    }

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Color getColor() {
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
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
