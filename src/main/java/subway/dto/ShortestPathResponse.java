package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private List<String> path;
    private double distance;
    private int cost;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(List<String> path, double distance, int cost) {
        this.path = path;
        this.distance = distance;
        this.cost = cost;
    }

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public int getCost() {
        return cost;
    }
}
