package subway.domain.station;

import java.util.Objects;

public class Station {

    private Long id;
    private StationName stationName;

    public Station(final Long id, final String stationName) {
        this.id = id;
        this.stationName = new StationName(stationName);
    }

    public Station(final String stationName) {
        this(null, stationName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return stationName.getName();
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
        return id.equals(station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
