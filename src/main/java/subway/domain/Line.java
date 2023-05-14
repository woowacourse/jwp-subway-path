package subway.domain;

import subway.entity.LineEntity;

import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;

    public Line(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Line(final String name) {
        this(null, name);
    }

    public LineEntity toEntity() {
        return new LineEntity(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                '}';
    }
}
