package subway.domain;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final List<Line> passLines;

    public Path(final List<Station> stations, final Distance distance, final List<Line> passLines) {
        this.stations = stations;
        this.distance = distance;
        this.passLines = passLines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public double getDistanceValue() {
        return distance.getValue();
    }

    public List<Line> getPassLines() {
        return passLines;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Path path = (Path) o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance) && Objects.equals(passLines, path.passLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, passLines);
    }
}
