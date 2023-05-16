package subway.dto.response;

import java.util.List;

public class LineStationResponse {
    private final List<String> stations;
    private final List<Integer> distances;

    public LineStationResponse(List<String> stations, List<Integer> distances) {
        this.stations = stations;
        this.distances = distances;
    }

    public List<String> getStations() {
        return stations;
    }

    public List<Integer> getDistances() {
        return distances;
    }
}
