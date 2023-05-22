package subway.domain.path;

import java.util.List;

public class Path {

    private final List<String> stations;
    private final int totalDistance;

    public Path(final List<String> stations, int totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
