package subway.domain;

import java.util.Objects;

public class Station {
    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public boolean isSameName(Station other) {
        return this.name.equals(other.name);
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
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
