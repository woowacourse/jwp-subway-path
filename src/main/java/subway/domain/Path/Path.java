package subway.domain.Path;

import subway.domain.fare.FarePolicy;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<Station> orderedStations;
    private final int distance;
    private final int fare;

    public Path(List<Station> orderedStations, int distance, int fare) {
        this.orderedStations = orderedStations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path from(List<Station> orderedStations, int pathDistance, int fare) {
        return new Path(new ArrayList<>(orderedStations), pathDistance, fare);
    }

    public List<Station> getOrderedStations() {
        return orderedStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
