package subway.path.infrastructure.shortestpath;

import static subway.path.exception.PathExceptionType.NOT_EXIST_STATION_IN_LINES;
import static subway.path.exception.PathExceptionType.NO_PATH;
import static subway.path.exception.PathExceptionType.START_AND_END_STATIONS_IS_SAME;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.path.domain.Path;
import subway.path.domain.ShortestRouteService;
import subway.path.exception.PathException;

@Component
public class JgraphtShortestRoute implements ShortestRouteService {

    private final LineDispatcher lineDispatcher;
    private final GraphCache graphCache;

    public JgraphtShortestRoute(final LineDispatcher lineDispatcher,
                                final GraphCache graphCache) {
        this.lineDispatcher = lineDispatcher;
        this.graphCache = graphCache;
    }

    @Override
    public Path shortestRoute(final Path path, final Station start, final Station end) {
        validateSameStation(start, end);
        final List<Section> shortestSections = findPath(path, start, end)
                .stream()
                .map(SectionAdapter::toSection)
                .collect(Collectors.toList());
        return lineDispatcher.dispatch(path, shortestSections);
    }

    private void validateSameStation(final Station start, final Station end) {
        if (start.equals(end)) {
            throw new PathException(START_AND_END_STATIONS_IS_SAME);
        }
    }

    private List<SectionAdapter> findPath(final Path lines, final Station start, final Station end) {
        final LinesGraphAdapter graph = graphCache.linesGraphAdapter(lines);
        final GraphPath<Station, SectionAdapter> path = validPath(graph, start, end);
        if (path == null) {
            throw new PathException(NO_PATH);
        }
        return path.getEdgeList();
    }

    private GraphPath<Station, SectionAdapter> validPath(
            final LinesGraphAdapter graph,
            final Station start,
            final Station end
    ) {
        try {
            return new DijkstraShortestPath<>(graph).getPath(start, end);
        } catch (final IllegalArgumentException e) {
            throw new PathException(NOT_EXIST_STATION_IN_LINES);
        }
    }
}
