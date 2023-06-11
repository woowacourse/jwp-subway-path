package subway.ui;

import subway.domain.Station;

import java.util.List;

public class PathResponse {
    private final List<Station> shortestPath;
    private final int distance;
    private final int fare;

    public PathResponse(List<Station> shortestPath, int distance, int fare) {
        this.shortestPath = shortestPath;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
