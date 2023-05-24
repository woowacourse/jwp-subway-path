package subway.station.domain;

import java.util.Objects;

public final class Station {

    private final Long id;
    private final StationName stationName;

    public Station(String stationName) {
        this(null, stationName);
    }

    public Station(Long id, String stationName) {
        this.id = id;
        this.stationName = StationName.from(stationName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return stationName.getValue();
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
        return Objects.equals(id, station.id) && Objects.equals(stationName, station.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stationName);
    }
}
