package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<String> stations;
    private final Integer distance;
    private final Integer fare;

    public PathResponse(List<String> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
