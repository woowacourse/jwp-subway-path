package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private final List<String> stations;
    private final int distance;
    private final int fare;

    public ShortestPathResponse(List<String> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
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

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
