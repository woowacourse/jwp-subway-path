package subway.domain.path;

import java.util.List;
import java.util.Set;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class SubwayPath {
    private final List<Station> stations;
    private final int distance;
    private final Set<Line> passingLine;

    public SubwayPath(final List<Station> stations, final int distance, final Set<Line> passingLine) {
        this.stations = stations;
        this.distance = distance;
        this.passingLine = passingLine;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getPassingLine() {
        return passingLine;
    }
}
