package subway.domain.path;

import subway.domain.section.Distance;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<String> stationNames;
    private final Distance distance;

    public Path(List<String> stationNames, int distance) {
        this.stationNames = stationNames;
        this.distance = new Distance(distance);
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(stationNames, path.stationNames) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationNames, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stationNames=" + stationNames +
                ", distance=" + distance +
                '}';
    }
}
