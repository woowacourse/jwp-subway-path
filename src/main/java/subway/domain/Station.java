package subway.domain;

import java.util.Objects;

public class Station {
    private final Long id;
    private final String name;

    private Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station createEmpty() {
        return new Station(null, null);
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public boolean isSameName(Station other) {
        return this.name.equals(other.name);
    }

    public boolean isEmpty(){
        return this.name == null;
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
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean isSameId(final long other) {
        return this.id == other;
    }
}
