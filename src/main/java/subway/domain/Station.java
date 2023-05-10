package subway.domain;

import java.util.List;
import java.util.Objects;

public class Station {
    private final String name;
    private final List<Line> lines;

    public Station(final String name, final List<Line> lines) {
        this.name = name;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(name, station.name) && Objects.equals(lines, station.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lines);
    }
}
