package subway.presentation.dto.response;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer cost;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer cost) {
        this.stations = stations;
        this.cost = cost;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getCost() {
        return cost;
    }
}
