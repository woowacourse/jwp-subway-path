package subway.dto;

import java.util.List;

public class PathResponse {

    private List<Long> shortestPath;
    private int distance;
    private int fee;

    public PathResponse() {
    }

    public PathResponse(List<Long> shortestPath, int distance, int fee) {
        this.shortestPath = shortestPath;
        this.distance = distance;
        this.fee = fee;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
