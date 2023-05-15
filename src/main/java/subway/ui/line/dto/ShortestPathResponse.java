package subway.ui.line.dto;

import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathResponse {

    private final double totalDistance;
    private final List<StationDto> stations;

    public ShortestPathResponse(final double pathWeight, final List<Station> stations) {
        this.totalDistance = pathWeight;
        this.stations = stations.stream()
                .map(m -> new StationDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}
