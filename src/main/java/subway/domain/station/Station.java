package subway.domain.station;

import java.util.Objects;

public class Station {

    private final Long id;
    private final StationName name;

    private Station(Long id, String name) {
        this.id = id;
        this.name = StationName.from(name);
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public static Station of(long id, String name) {
        return new Station(id, name);
    }

    public boolean isSameName(Station other) {
        return this.name.equals(other.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getStationName();
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
