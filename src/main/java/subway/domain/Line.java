package subway.domain;

import java.util.Objects;

public class Line {
    private Long id;
    private String name;

    public Line(String name) {
        this.name = name;
    }

    public Line(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
