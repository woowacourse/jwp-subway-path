package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<String> shortestPath;
    private final int shortestPathDistance;
    private final int fare;

    public PathResponse(List<String> shortestPath, int shortestPathDistance, int fare) {
        this.shortestPath = shortestPath;
        this.shortestPathDistance = shortestPathDistance;
        this.fare = fare;
    }

    public List<String> getShortestPath() {
        return shortestPath;
    }

    public int getShortestPathDistance() {
        return shortestPathDistance;
    }

    public int getFare() {
        return fare;
    }
}
