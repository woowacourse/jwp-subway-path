package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private final List<String> stations;
    private final double distance;

    public ShortestPathResponse(List<String> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                '}';
    }

    public List<String> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
