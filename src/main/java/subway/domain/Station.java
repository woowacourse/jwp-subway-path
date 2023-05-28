package subway.domain;

import java.util.Objects;

public class Station {

    private String name;

    public Station(final String name) {
        this.name = name;
    }

    public boolean isSame(final Station other) {
        return name.equals(other.name);
    }

    public void updateStationName(final Station station) {
        name = station.name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
