package subway.dto;

import java.util.List;

public class PathResponse {

    private List<String> path;
    private int distance;
    private int cost;

    public PathResponse() {
    }

    public PathResponse(List<String> path, int distance, int cost) {
        this.path = path;
        this.distance = distance;
        this.cost = cost;
    }

    public List<String> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost() {
        return cost;
    }
}
