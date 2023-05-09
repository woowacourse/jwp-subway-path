package subway.domain.station;

import java.util.LinkedList;
import java.util.List;

public class Stations {
    private final List<StationOnLine> stations;

    public Stations() {
        this.stations = new LinkedList<>();
    }

    public void addInitialStations(
            final Station from,
            final Station to,
            final StationDistance distance
    ) {
        if (!stations.isEmpty()) {
            throw new IllegalStateException();
        }

        final StationOnLine start = new StationOnLine(from, null, distance);
        final StationOnLine end = new StationOnLine(to, distance, null);

        stations.add(start);
        stations.add(end);
    }

    public List<StationOnLine> getStations() {
        return stations;
    }
}
