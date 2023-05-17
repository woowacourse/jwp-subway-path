package subway.domain;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final double distance;

    private Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, double distance) {
        return new Path(stations, distance);
    }



}
