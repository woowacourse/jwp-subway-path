package subway.domain;

import java.util.Objects;

public class Station {
    private final Long id;
    private final String name;

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Station(final String name) {
        this(null, name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        if (id == null || station.id == null) {
            return Objects.equals(name, station.name);
        }
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
