package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<String> stations;
    private final Integer distance;

    public PathResponse(List<String> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<String> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
