package subway.domain;

import java.util.Objects;

public final class LineStation {

    private Line line;
    private Station station;

    private LineStation(final Line line, final Station station) {
        this.line = line;
        this.station = station;
    }

    public static LineStation of(final Line line, final Station station) {
        return new LineStation(line, station);
    }

    public static LineStation withNullLine(final Station station) {
        return new LineStation(null, station);
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineStation that = (LineStation) o;
        return Objects.equals(station, that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station);
    }
}
