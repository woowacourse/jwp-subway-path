package subway.adapter.out.graph.dto;

import subway.domain.Station;

import java.util.List;
import java.util.Set;

public class RouteDto {
    private final List<Station> stations;
    private final int distance;
    private final Set<Long> lineIds;

    public RouteDto(final List<Station> stations, final int distance, final Set<Long> lineIds) {
        this.stations = stations;
        this.distance = distance;
        this.lineIds = lineIds;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getLineIds() {
        return List.copyOf(lineIds);
    }
}
