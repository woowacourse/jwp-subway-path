package subway.path.presentation.dto.response;

import subway.station.presentation.dto.response.StationResponse;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations;
    private final double distance;
    private final Integer cost;

    public PathResponse(List<StationResponse> stations, double distance, Integer cost) {
        this.stations = stations;
        this.distance = distance;
        this.cost = cost;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public Integer getCost() {
        return cost;
    }

}
