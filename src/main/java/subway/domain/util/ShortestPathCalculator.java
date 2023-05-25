package subway.domain.util;

import subway.domain.Line;
import subway.domain.Path;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.support.Dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathCalculator {

    private static final Dijkstra<Station> DIJKSTRA = new Dijkstra<>();

    public static ShortestPath calculate(final Station start, final Station end, final List<Line> lines) {
        final List<Station> starts = new ArrayList<>();
        final List<Station> ends = new ArrayList<>();
        final List<Integer> distances = new ArrayList<>();
        parseLines(lines, starts, ends, distances);

        final var result = DIJKSTRA.shortestPath(starts, ends, distances, start, end);

        try {
            return new ShortestPath(((int) result.getWeight()), result.getVertexList());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }

    private static void parseLines(final List<Line> lines, final List<Station> starts, final List<Station> ends, final List<Integer> distances) {
        for (final Line line : lines) {
            final Map<Station, Path> paths = line.getPaths();
            parseLine(starts, ends, distances, paths);
        }
    }

    private static void parseLine(final List<Station> starts, final List<Station> ends, final List<Integer> distances, final Map<Station, Path> paths) {
        for (final Map.Entry<Station, Path> entry : paths.entrySet()) {
            starts.add(entry.getKey());
            ends.add(entry.getValue().getNext());
            distances.add(entry.getValue().getDistance());
        }
    }
}
