package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Section;
import subway.service.ShortestPath;

import java.util.List;

@AllArgsConstructor
@Getter
public class PathResponse {
    private final List<Section> path;
    private final Distance distance;
    private final Fare fare;

    public static PathResponse of(ShortestPath shortestPath, Fare fare) {
        return new PathResponse(shortestPath.getPath(), shortestPath.getDistance(), fare);
    }
}
