package subway.application.core.service.dto.out;

import subway.application.core.domain.Station;

import java.util.List;

public class PathFindResult {

    private final List<Station> shortestPath;
    private final Double distance;

    public PathFindResult(List<Station> shortestPath, Double distance) {
        this.shortestPath = shortestPath;
        this.distance = distance;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public Double getDistance() {
        return distance;
    }
}
