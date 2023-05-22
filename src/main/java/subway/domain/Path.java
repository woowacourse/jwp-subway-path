package subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, Distance distance) {
        return new Path(stations, distance);
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
