package subway.domain;

import java.util.Objects;

public class Station {
    private final Long id;
    private final Name name;

    public Station(final String name) {
        this(null, name);
    }

    public Station(final Long id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public Station(final Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public boolean equalsName(Station station) {
        return this.name.equals(station.name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
