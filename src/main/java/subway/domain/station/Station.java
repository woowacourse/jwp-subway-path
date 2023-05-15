package subway.domain.station;

import java.util.Objects;

public class Station {
    private final Long stationId;
    private final StationName stationName;

    public Station(final String stationName) {
        this(null, new StationName(stationName));
    }

    public Station(final Long stationId, final StationName stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public boolean matchStationName(final Station other) {
        return this.getStationName().equals(other.getStationName());
    }

    public Long getStationId() {
        return stationId;
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
        return Objects.equals(getStationId(), station.getStationId()) && Objects.equals(
                getStationName(), station.getStationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStationId(), getStationName());
    }
}
