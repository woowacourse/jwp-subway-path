package subway.dto;

import subway.domain.path.Fee;
import subway.domain.path.Path;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final int fee;
    private final int distance;
    private final List<String> stations;

    public PathResponse(int fee, int distance, List<String> stations) {
        this.fee = fee;
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(final Fee fee, final Path path) {
        List<String> stations = path.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        return new PathResponse(fee.getFee(), path.getDistance(), stations);
    }

    public int getFee() {
        return fee;
    }

    public int getDistance() {
        return distance;
    }

    public List<String> getStations() {
        return stations;
    }
}
