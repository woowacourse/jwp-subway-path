package subway.domain;

import java.util.Objects;

public final class Line {
    private Long id;
    private final String name;
    private final String color;
    private Paths paths;

    public Line(final Long id, final String name, final String color, final Paths paths) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.paths = paths;
    }

    public Line(final String name, final String color, final Paths paths) {
        this(null, name, color, paths);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new Paths());
    }

    public Line(final String name, final String color) {
        this(null, name, color, new Paths());
    }

    public Line(final Line line) {
        this(line.id, line.name, line.color, line.paths);
    }

    public Line addPath(final Path path) {
        final Line line = new Line(this);
        line.paths = paths.addPath(path);

        return line;
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

    public int getPathsSize() {
        return paths.toList().size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
