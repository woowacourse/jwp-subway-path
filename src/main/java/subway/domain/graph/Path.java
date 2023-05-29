package subway.domain.graph;

import java.util.List;
import subway.domain.fare.Fare;
import subway.domain.fare.FareCalculator;
import subway.domain.section.Distance;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class Path {
    private final Distance distance;
    private final List<Station> stations;
    private final Fare fare;

    private Path(final Distance distance, final List<Station> stations, final Fare fare) {
        this.distance = distance;
        this.stations = stations;
        this.fare = fare;
    }

    public static Path of(final Station source, final Station destination, SubwayGraph graph, Sections sections,
                          final FareCalculator fareCalculator) {
        int distance = graph.getWeight(sections.getSections(), source, destination);
        List<Station> path = graph.getPath(sections.getSections(), source, destination);
        return new Path(new Distance(distance), path, fareCalculator.calculate(distance));
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Fare getFare() {
        return fare;
    }
}
