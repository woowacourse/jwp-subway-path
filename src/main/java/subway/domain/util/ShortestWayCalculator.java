package subway.domain.util;

import subway.domain.Line;
import subway.domain.Path;
import subway.domain.ShortestWay;
import subway.domain.Station;
import subway.support.Dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestWayCalculator {

    private static final Dijkstra<Station> dijkstra = new Dijkstra<>();

    public static ShortestWay calculate(final Station start, final Station end, final List<Line> lines) {
        final List<Station> starts = new ArrayList<>();
        final List<Station> ends = new ArrayList<>();
        final List<Integer> distances = new ArrayList<>();
        mappingPath(lines, starts, ends, distances);

        final var result = dijkstra.shortestPath(starts, ends, distances, start, end);

        try {
            return new ShortestWay(((int) result.getWeight()), result.getVertexList());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }

    private static void mappingPath(final List<Line> lines, final List<Station> starts, final List<Station> ends, final List<Integer> distances) {
        for (final Line line : lines) {
            final Map<Station, Path> paths = line.getPaths();
            for (final Map.Entry<Station, Path> entry : paths.entrySet()) {
                starts.add(entry.getKey());
                ends.add(entry.getValue().getNext());
                distances.add(entry.getValue().getDistance());
            }
        }
    }
}
