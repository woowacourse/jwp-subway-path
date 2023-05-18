package subway.ui.line.dto;

import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathResponse {

    private final int totalDistance;
    private final List<StationDto> stations;
    private final int fare;

    public ShortestPathResponse(final int pathWeight, final List<Station> stations, final int fare) {
        this.totalDistance = pathWeight;
        this.stations = stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
        this.fare = fare;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }
}
