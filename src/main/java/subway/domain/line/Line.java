package subway.domain.line;

import java.util.Objects;

public class Line {

    private final Long id;
    private final LineName name;

    public Line(Long id, String name) {
        this.id = id;
        this.name = new LineName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getLineName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line that = (Line) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
