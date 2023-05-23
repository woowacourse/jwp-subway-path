package subway.domain.station;

import java.util.Objects;

public class Station {
    private final Long id;
    private final StationName name;

    public Station(final Long id, final StationName name) {
        this.id = id;
        this.name = name;
    }

    public Station(final Long id) {
        this(id, null);
    }

    public Station(final StationName name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
