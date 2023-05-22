package subway.domain.station;

import java.util.Objects;

public class Station {

    private final StationName name;

    private Station(final StationName name) {
        this.name = name;
    }

    public static Station create(final String name) {
        return new Station(StationName.create(name));
    }

    public static Station empty() {
        return new Station(StationName.empty());
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
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public StationName name() {
        return name;
    }
}
