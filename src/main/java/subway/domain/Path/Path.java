package subway.domain.Path;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<Station> orderedStations;
    private final int distance;
    private final Fare fare;

    public Path(List<Station> orderedStations, int distance, Fare fare) {
        this.orderedStations = orderedStations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path from(List<Station> orderedStations, int pathDistance, Fare fare) {
        return new Path(new ArrayList<>(orderedStations), pathDistance, fare);
    }

    public List<Station> getOrderedStations() {
        return orderedStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
