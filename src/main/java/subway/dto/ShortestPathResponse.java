package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private List<String> path;
    private double distance;
    private int fare;

    public ShortestPathResponse(final List<String> path, final double distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
