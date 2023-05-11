package subway.domain;

import java.util.Objects;

public class StationLine {

    private final Long id;
    private final Station station;
    private final Line line;

    public StationLine(final Station station, final Line line) {
        this(null, station, line);
    }

    public StationLine(final Long id, final Station station, final Line line) {
        this.id = id;
        this.station = station;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationLine that = (StationLine) o;
        return Objects.equals(id, that.id) && Objects.equals(station, that.station) && Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, station, line);
    }

    @Override
    public String toString() {
        return "StationLine{" +
                "id=" + id +
                ", station=" + station +
                ", line=" + line +
                '}';
    }
}
