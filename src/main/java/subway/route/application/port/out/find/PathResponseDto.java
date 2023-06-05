package subway.route.application.port.out.find;

import java.util.List;
import subway.route.domain.InterStationEdge;

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
