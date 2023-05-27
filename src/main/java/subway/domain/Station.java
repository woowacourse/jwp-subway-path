package subway.domain;

import java.util.Objects;

public class Station {

    private final String name;

    public Station(final String name) {
        this.name = name;
    }

    public boolean isSame(final String name) {
        return this.name.equals(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
