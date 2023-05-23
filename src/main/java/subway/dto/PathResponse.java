package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;
import subway.domain.path.ShortestPath;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int charge;

    public PathResponse(List<StationResponse> stations, int distance, int charge) {
        this.stations = stations;
        this.distance = distance;
        this.charge = charge;
    }

    public static PathResponse of(ShortestPath path, int fee) {
        List<StationResponse> stations = path.passingStation().stream()
                .map(Station::getName)
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance(), fee);
    }

    public int getDistance() {
        return distance;
    }

    public int getCharge() {
        return charge;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
