package subway.route.dto.response;

import java.util.List;
import subway.route.domain.InterStationEdge;

public class PathResponse {

    private final int distance;
    private final List<InterStationEdge> stations;

    public PathResponse(int distance, List<InterStationEdge> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<InterStationEdge> getStations() {
        return stations;
    }
}
