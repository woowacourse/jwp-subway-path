package subway.dto.path;

import java.util.List;

public class PathResponse {
    private List<String> stations;
    private int totalDistance;
    private int totalPrice;

    public PathResponse(List<String> stations, int totalDistance, int totalPrice) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalPrice = totalPrice;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
