package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.path.ShortestPath;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fee;

    public PathResponse(List<StationResponse> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse of(ShortestPath path, int fee) {
        List<StationResponse> stations = path.passingStation().stream()
                .map(it -> new StationResponse(it.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance(), fee);
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
