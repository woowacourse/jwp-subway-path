package subway.dto.path;

import java.util.List;

public class PathResponse {
    private List<String> stations;
    private int totalDistance;
    private long price;

    public PathResponse(List<String> stations, int totalDistance, long price) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.price = price;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public long getPrice() {
        return price;
    }
}
