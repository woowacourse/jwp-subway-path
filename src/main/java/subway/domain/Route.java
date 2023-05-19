package subway.domain;

import java.util.List;

public class Route {
    private static final int BASIC_RATE = 1250;
    private final List<Station> stations;
    private final Long distance;

    public Route(final List<Station> stations, final Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public Fare calculateOverFare() {
        long totalDistance = distance;
        long total = BASIC_RATE;

        total += Math.max(totalDistance - 50, 0) / 8L * 100;
        total += Math.max(totalDistance - 10, 0) / 5L * 100;

        return new Fare(total);
    }

    public List<Station> getStations() {
        return stations;
    }
}
