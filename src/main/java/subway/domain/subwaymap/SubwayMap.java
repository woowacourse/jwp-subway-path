package subway.domain.subwaymap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import subway.domain.pathfinder.ShortestPath;
import subway.domain.pathfinder.SubwayMapShortestPathFinder;
import subway.exception.custom.LineDoesNotContainStationException;

public class SubwayMap {

    private final Map<Line, Sections> linesAndSections;
    private final SubwayMapShortestPathFinder subwayMapShortestPathFinder;

    public SubwayMap(final List<Line> lines, final List<Sections> linePaths,
        final SubwayMapShortestPathFinder subwayMapShortestPathFinder) {
        if (lines == null || linePaths == null || lines.size() != linePaths.size()) {
            throw new IllegalArgumentException("지하철 노선도를 생성하기에 부적절한 정보입니다.");
        }
        this.linesAndSections = IntStream.range(0, lines.size())
            .boxed()
            .collect(Collectors.toMap(lines::get, linePaths::get));
        this.subwayMapShortestPathFinder = subwayMapShortestPathFinder;
    }

    public ShortestPath getShortestPath(final Station start, final Station end) {
        return subwayMapShortestPathFinder.getShortestPath(linesAndSections, start, end);
    }

    public Line findLineContains(final Station station) {
        return linesAndSections.entrySet()
            .stream()
            .filter((entry) -> entry.getValue().getStations().contains(station))
            .findFirst()
            .orElseThrow(() -> new LineDoesNotContainStationException(String.format("%s 역을 포함한 라인이 존재하지 않습니다.",
                station.getName()))).getKey();
    }
}
