package subway.infrastructure.shortestpath;

import static subway.exception.line.LineExceptionType.NOT_EXIST_STATION_IN_LINES;
import static subway.exception.line.LineExceptionType.NO_PATH;
import static subway.exception.line.LineExceptionType.START_AND_END_STATIONS_IS_SAME;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.service.ShortestRouteService;
import subway.exception.line.LineException;

@Component
public class JgraphtShortestRoute implements ShortestRouteService {

    private final Map<Lines, LinesGraphAdapter> cache = new ConcurrentHashMap<>();

    @Override
    public Lines shortestRoute(final Lines lines, final Station start, final Station end) {
        validateSameStation(start, end);
        final List<Section> shortestSections = findPath(lines, start, end)
                .stream()
                .map(SectionAdapter::toSection)
                .collect(Collectors.toList());
        return makeShortestLines(lines, shortestSections);
    }

    private void validateSameStation(final Station start, final Station end) {
        if (start.equals(end)) {
            throw new LineException(START_AND_END_STATIONS_IS_SAME);
        }
    }

    private List<SectionAdapter> findPath(final Lines lines, final Station start, final Station end) {
        final LinesGraphAdapter graph = findGraph(lines);
        final GraphPath<Station, SectionAdapter> path = validPath(graph, start, end);
        if (path == null) {
            throw new LineException(NO_PATH);
        }
        return path.getEdgeList();
    }

    private LinesGraphAdapter findGraph(final Lines lines) {
        if (!cache.containsKey(lines)) {
            cache.clear();
            cache.put(lines, LinesGraphAdapter.adapt(lines));
        }
        return cache.get(lines);
    }

    private GraphPath<Station, SectionAdapter> validPath(
            final LinesGraphAdapter graph,
            final Station start,
            final Station end
    ) {
        try {
            return new DijkstraShortestPath<>(graph).getPath(start, end);
        } catch (final IllegalArgumentException e) {
            throw new LineException(NOT_EXIST_STATION_IN_LINES);
        }
    }

    private Lines makeShortestLines(final Lines lines, final List<Section> shortestSections) {
        final List<Line> result = new ArrayList<>();
        final Deque<Section> deque = new ArrayDeque<>(shortestSections);
        while (!deque.isEmpty()) {
            result.add(sectionOwner(lines, deque));
        }
        return new Lines(result);
    }

    private Line sectionOwner(final Lines lines, final Deque<Section> sections) {
        final Line sectionOwner = findSectionOwner(lines, sections);
        final List<Section> result = addSectionsToLine(sectionOwner, sections);
        return new Line(sectionOwner.id(), sectionOwner.name(), toSections(result));
    }

    private Line findSectionOwner(final Lines lines, final Deque<Section> sections) {
        return lines.lines()
                .stream()
                .filter(it -> it.contains(sections.peekFirst()))
                .findAny()
                .orElseThrow(() -> new LineException("최단경로 구하는 중 문제 발생"));
    }

    private List<Section> addSectionsToLine(final Line sectionsContainLine, final Deque<Section> sections) {
        final List<Section> result = new ArrayList<>();
        while (isSectionsOwner(sectionsContainLine, sections)) {
            result.add(sections.pollFirst());
        }
        return result;
    }

    private boolean isSectionsOwner(final Line sectionsContainLine, final Deque<Section> sections) {
        return !sections.isEmpty() && sectionsContainLine.contains(sections.peekFirst());
    }

    private Sections toSections(final List<Section> result) {
        try {
            return new Sections(result);
        } catch (final LineException e) {
            Collections.reverse(result);
            return new Sections(result);
        }
    }
}
