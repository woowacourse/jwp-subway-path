package subway.dto.service;

import java.util.List;
import java.util.Map;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;

public class PathResult {
    private final Station departure;
    private final Station arrival;

    private final Map<Line, List<Station>> lineToStations;
    private final Path path;

    public PathResult(Station departure, Station arrival, Map<Line, List<Station>> lineToStations,
                      Path path) {
        this.departure = departure;
        this.arrival = arrival;
        this.lineToStations = lineToStations;
        this.path = path;
    }

    public Station getDeparture() {
        return departure;
    }

    public Station getArrival() {
        return arrival;
    }

    public Map<Line, List<Station>> getLineToStations() {
        return lineToStations;
    }

    public Path getPath() {
        return path;
    }
}
