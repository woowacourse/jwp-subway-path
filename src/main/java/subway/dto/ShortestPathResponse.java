package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private List<String> path;
    private double distance;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(List<String> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}
