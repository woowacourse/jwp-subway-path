package subway.dto.response;

import java.util.List;

public class PathResponse {
    private final List<String> stations;
    private final int distance;
    private final int fare;

    public PathResponse(final List<String> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
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
