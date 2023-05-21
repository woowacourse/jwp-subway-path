package subway.dto.route;

import subway.domain.subway.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PathsResponse {

    private final List<PathResponse> paths;
    private final int fee;

    public PathsResponse(final List<PathResponse> paths, final int fee) {
        this.paths = paths;
        this.fee = fee;
    }

    public static PathsResponse from(final List<Station> stations, List<Set<String>> transferLines, final int fee) {
        List<PathResponse> paths = new ArrayList<>();

        for (int i = 0; i < stations.size(); i++) {
            paths.add(PathResponse.from(stations.get(i), transferLines.get(i)));
        }

        return new PathsResponse(paths, fee);
    }

    public List<PathResponse> getPaths() {
        return paths;
    }

    public int getFee() {
        return fee;
    }
}
