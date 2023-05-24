package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import subway.domain.line.Line;
import subway.domain.path.Path;
import subway.domain.path.PathSections;
import subway.domain.path.ShortestPathCalculator;
import subway.domain.path.graph.PathEdges;
import subway.domain.path.graph.PathGraph;
import subway.domain.station.Station;

@Component
public class JGraphtShortestPathCalculator implements ShortestPathCalculator {

    @Override
    public Path findPath(final List<Line> lines, final Station sourceStation, final Station targetStation) {
        validateDuplicateStation(sourceStation, targetStation);

        final PathGraph pathGraph = PathGraph.from(lines);

        try {
            final List<PathEdges> shortestPathEdges = pathGraph.findShortestPathSections(
                    sourceStation, targetStation);

            final List<PathSections> shortestPathSections = shortestPathEdges.stream()
                    .map(PathEdges::to)
                    .collect(Collectors.toList());

            return Path.from(shortestPathSections);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 역은 노선에 등록되지 않은 역입니다.", e);
        }
    }

    private void validateDuplicateStation(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발지와 목적지가 동일할 수 없습니다.");
        }
    }
}
