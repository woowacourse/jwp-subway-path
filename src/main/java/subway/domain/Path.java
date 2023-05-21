package subway.domain;

import java.util.List;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.station.Station;

public final class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final List<Line> borderedLines;

    public Path(final List<Station> stations, final List<Line> borderedLines, final int distance) {
        this.stations = stations;
        this.borderedLines = borderedLines;
        this.distance = new Distance(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getBorderedLines() {
        return borderedLines;
    }

    public Distance getDistance() {
        return distance;
    }
}
