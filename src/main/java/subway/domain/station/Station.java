package subway.domain.station;

import java.util.Objects;

public class Station {

    private final Long id;
    private final StationName name;

    private Station(final Long id, final String name) {
        this.id = id;
        this.name = StationName.from(name);
    }

    public static Station from(final String name) {
        return new Station(null, name);
    }

    public static Station of(final Station station) {
        return new Station(station.id, station.name.getName());
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
