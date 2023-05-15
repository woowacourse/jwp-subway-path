package subway.service.dto;

import java.util.List;

public class ShortestRouteResponse {

    private List<String> stations;

    public ShortestRouteResponse(final List<String> stations) {
        this.stations = stations;
    }

    public List<String> getStations() {
        return stations;
    }
}
