package subway.route.dto.response;

import java.util.List;
import subway.route.domain.InterStationEdge;

public class RouteFindResponse {

    private final List<InterStationEdge> stations;
    private final long distance;
    private final long fare;

    public RouteFindResponse(List<InterStationEdge> stations, long distance, long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<InterStationEdge> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
