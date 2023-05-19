package subway.application.route.port.out.find;

import java.util.List;
import subway.domain.route.InterStationEdge;

public class PathResponseDto {

    private final int distance;
    private final List<InterStationEdge> stations;

    public PathResponseDto(final int distance, final List<InterStationEdge> stations) {
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
