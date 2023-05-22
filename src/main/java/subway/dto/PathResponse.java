package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<String> shortestPath;
    private final int shortestPathDistance;

    public PathResponse(List<String> shortestPath, int shortestPathDistance) {
        this.shortestPath = shortestPath;
        this.shortestPathDistance = shortestPathDistance;
    }

    public List<String> getShortestPath() {
        return shortestPath;
    }

    public int getShortestPathDistance() {
        return shortestPathDistance;
    }
}
