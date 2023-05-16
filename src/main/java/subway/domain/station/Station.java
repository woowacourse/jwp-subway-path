package subway.domain.station;

import java.util.Objects;

public class Station {
    private final Long id;
    private final StationName stationName;

    private Station(Long id, String name) {
        this.id = id;
        this.stationName = StationName.from(name);
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public static Station of(long id, String name) {
        return new Station(id, name);
    }

    public boolean isSameName(Station other) {
        return this.stationName.equals(other.stationName);
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName.getStationName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return stationName.equals(station.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationName);
    }
}
