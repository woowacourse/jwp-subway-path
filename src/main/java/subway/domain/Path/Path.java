package subway.domain.Path;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<Station> pathStations;
    private final int distance;
    private final Fare fare;

    public Path(List<Station> pathStations, int distance, Fare fare) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path from(List<Station> orderedStations, int pathDistance, Fare fare) {
        return new Path(new ArrayList<>(orderedStations), pathDistance, fare);
    }

    public List<Station> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
