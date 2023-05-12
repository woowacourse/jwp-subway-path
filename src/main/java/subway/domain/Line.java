package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Line {
    private Long id;
    private final String name;
    private final String color;
    private List<Station> stations = new ArrayList<>();

    public Line(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Line(final String name, final String color, final List<Station> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
