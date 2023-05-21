package subway.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Distance;
import subway.domain.Section;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShortestPath {
    private final List<Section> path;
    private final Distance distance;

    public static ShortestPath of(List<Section> path, Distance distance) {
        return new ShortestPath(path, distance);
    }
}
