package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.ShortestPath;

import java.util.List;

@AllArgsConstructor
@Getter
public class PathResponse {
    private final List<Section> path;
    private final Distance distance;
    private final int fare;

    public static PathResponse of(ShortestPath shortestPath) {
        return new PathResponse(shortestPath.getPath(), shortestPath.getDistance(), shortestPath.getFare());
    }
}
