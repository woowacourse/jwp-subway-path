package subway.dto.route;

import subway.domain.subway.Station;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PathsResponse {

    private final List<PathResponse> paths;
    private final int fee;

    public PathsResponse(final List<PathResponse> paths, final int fee) {
        this.paths = paths;
        this.fee = fee;
    }

    public static PathsResponse from(final Map<Station, Set<String>> lineNamesByStation, final int fee) {
        List<PathResponse> paths = lineNamesByStation.keySet().stream()
                .map(station -> PathResponse.from(station, lineNamesByStation.get(station)))
                .collect(Collectors.toList());

        return new PathsResponse(paths, fee);
    }

    public List<PathResponse> getPaths() {
        return paths;
    }

    public int getFee() {
        return fee;
    }
}
