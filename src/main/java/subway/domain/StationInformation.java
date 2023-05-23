package subway.domain;

import java.util.Objects;

public class StationInformation {

    private final Station station;
    private final Line line;

    public StationInformation(final Station station, final Line line) {
        this.station = station;
        this.line = line;
    }

    public boolean isEqualStation(final Station station) {
        return this.station.equals(station);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationInformation that = (StationInformation) o;
        return Objects.equals(station, that.station) && Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, line);
    }

    public long getLineCharge() {
        return line.getCharge();
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }
}
