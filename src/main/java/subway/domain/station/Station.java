package subway.domain.station;

import java.util.Objects;

public class Station {

    private final StationName name;

    private Station(String name) {
        this.name = StationName.from(name);
    }

    public static Station from(String name) {
        return new Station(name);
    }

    public boolean isSameName(Station other) {
        return this.name.equals(other.name);
    }

    public String getName() {
        return name.getStationName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station other = (Station) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
