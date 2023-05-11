package subway.entity;

import java.util.List;
import java.util.Objects;

public class Station {

    private Long id;
    private String name;

    private Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static Station of(final String name) {
        return new Station(null, name);
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isContainStations(final List<Station> stations) {
        for (Station otherStation : stations) {
            if (this.name.equals(otherStation.name)) {
                return true;
            }
        }

        return false;
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
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
