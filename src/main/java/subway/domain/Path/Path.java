package subway.domain.Path;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<Station> orderedStations;
    private final int distance;

    private Path(List<Station> orderedStations, int distance) {
        this.orderedStations = orderedStations;
        this.distance = distance;
    }

    public static Path from(List<Station> orderedStations, int pathDistance) {
        return new Path(new ArrayList<>(orderedStations), pathDistance);
    }

    public List<Station> getOrderedStations() {
        return orderedStations;
    }

    public int getDistance() {
        return distance;
    }
}
