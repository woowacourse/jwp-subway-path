package subway.domain.station;

import subway.exception.IllegalStationNameException;

import java.util.Objects;

public class StationName {

    public static final int MAX_STATION_NAME_LENGTH = 10;
    private final String stationName;

    public StationName(String stationName) {
        validate(stationName);
        this.stationName = stationName;
    }

    private void validate(String stationName) {
        if (stationName.length() > MAX_STATION_NAME_LENGTH) {
            throw new IllegalStationNameException();
        }
    }

    public String getStationName() {
        return stationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationName that = (StationName) o;
        return Objects.equals(stationName, that.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationName);
    }

    @Override
    public String toString() {
        return "StationName{" +
                "stationName='" + stationName + '\'' +
                '}';
    }
}
