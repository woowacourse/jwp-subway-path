package subway.domain.Path;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<Station> orderedStations;

    private Path(List<Station> orderedStations) {
        this.orderedStations = orderedStations;
    }

    public static Path from(List<Station> orderedStations) {
        return new Path(new ArrayList<>(orderedStations));
    }

    public List<Station> getOrderedStations() {
        return orderedStations;
    }
}
