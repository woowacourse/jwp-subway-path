package subway.dto.service;

import java.util.List;
import java.util.Map;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;

public class PathResult {
    private final Map<Line, List<Station>> lineToStations;
    private final Path path;

    public PathResult(Map<Line, List<Station>> lineToStations, Path path) {
        this.lineToStations = lineToStations;
        this.path = path;
    }

    public Map<Line, List<Station>> getLineToStations() {
        return lineToStations;
    }

    public Path getPath() {
        return path;
    }
}
