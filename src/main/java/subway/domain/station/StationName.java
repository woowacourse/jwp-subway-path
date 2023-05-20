package subway.domain.station;

import java.util.Objects;

public class StationName {

    private final String StationName;

    public StationName(String stationName) {
        StationName = stationName;
    }

    public static StationName from(String name) {
        return new StationName(name);
    }

    public String getStationName() {
        return StationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationName that = (StationName) o;
        return Objects.equals(StationName, that.StationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StationName);
    }
}
