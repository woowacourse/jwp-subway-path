package subway.domain;

import java.util.Objects;

public final class Station {
    private final Long id;
    private final String name;

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Station(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        if (id != null) {
            return id.equals(station.id);
        }
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}