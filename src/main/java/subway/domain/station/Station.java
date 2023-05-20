package subway.domain.station;

import java.util.Objects;

public class Station {

    private final StationName name;

    public Station(String name) {
        this.name = new StationName(name);
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
