package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final Integer distance;
    private final Integer cost;

    public PathResponse(List<StationResponse> stations, Integer distance, Integer cost) {
        this.stations = stations;
        this.distance = distance;
        this.cost = cost;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getCost() {
        return cost;
    }
}
