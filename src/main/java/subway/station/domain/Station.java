package subway.station.domain;

import java.util.Objects;

public class Station {
    private final StationName stationName;

    public Station(final String stationName) {
        this(new StationName(stationName));
    }

    public Station(final StationName stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName.getName();
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
        return Objects.equals(getStationName(), station.getStationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStationName());
    }
}
