package subway.domain.line;

import java.util.Objects;

public class Line {

    private final Long id;
    private final LineName name;
    private final Surcharge surcharge;

    public Line(Long id, String name, int surcharge) {
        this.id = id;
        this.name = new LineName(name);
        this.surcharge = new Surcharge(surcharge);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getLineName();
    }

    public int getSurcharge() {
        return surcharge.getSurcharge();
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(surcharge, line.surcharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surcharge);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name=" + name +
                ", surcharge=" + surcharge +
                '}';
    }
}
