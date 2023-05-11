package subway.domain.station;

import java.util.Objects;

public class Station {

    private final StationName name;

    public Station(String name) {
        this.name = new StationName(name);
    }

    public String getName() {
        return name.getStationName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
                "name=" + name +
                '}';
    }
}
