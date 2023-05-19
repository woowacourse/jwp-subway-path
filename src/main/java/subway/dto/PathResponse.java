package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.ShortestPath;
import subway.domain.Station;

import java.util.List;

@AllArgsConstructor
@Getter
public class PathResponse {
    private final List<Station> path;
    private final double distance;
    private final int fare;

    public static PathResponse of(ShortestPath shortestPath) {
        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), shortestPath.getFare());
    }
}
