package subway.infrastructure.shortestpath;

import static subway.exception.line.LineExceptionType.NOT_EXIST_STATION_IN_LINES;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.LinkedRoute;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.service.ShortestRouteService;
import subway.exception.line.LineException;
import subway.exception.line.LineExceptionType;

@Component
public class JgraphtShortestRoute implements ShortestRouteService {

    @Override
    public LinkedRoute shortestRoute(final List<Line> lines, final Station start, final Station end) {
        validateSameStation(start, end);
        final List<Section> shortestSections = findPath(lines, start, end)
                .stream()
                .map(SectionAdapter::toSection)
                .collect(Collectors.toList());
        return toRoute(start, lines, shortestSections);
    }

    private void validateSameStation(final Station start, final Station end) {
        if (start.equals(end)) {
            throw new LineException(LineExceptionType.START_AND_END_STATIONS_IS_SAME);
        }
    }

    private List<SectionAdapter> findPath(
            final List<Line> lines,
            final Station start,
            final Station end
    ) {
        final GraphPath<Station, SectionAdapter> path = validPath(lines, start, end);
        if (path == null) {
            return Collections.emptyList();
        }
        return path.getEdgeList();
    }

    private static GraphPath<Station, SectionAdapter> validPath(
            final List<Line> lines,
            final Station start,
            final Station end
    ) {
        final LinesGraphAdapter graph = LinesGraphAdapter.adapt(lines);
        final DijkstraShortestPath<Station, SectionAdapter> shortestPath = new DijkstraShortestPath<>(graph);
        try {
            return shortestPath.getPath(start, end);
        } catch (final IllegalArgumentException e) {
            throw new LineException(NOT_EXIST_STATION_IN_LINES);
        }
    }

    private LinkedRoute toRoute(final Station start, final List<Line> lines, final List<Section> shortestSections) {
        final List<Line> result = new ArrayList<>();
        final Deque<Section> deque = new ArrayDeque<>(shortestSections);
        while (!deque.isEmpty()) {
            result.add(getSectionContainsLine(lines, deque));
        }
        return LinkedRoute.of(start, result);
    }

    private Line getSectionContainsLine(final List<Line> lines, final Deque<Section> sections) {
        final Line sectionsContainLine = findSectionContainsLine(lines, sections);
        final List<Section> result = addSectionsToLine(sectionsContainLine, sections);
        return new Line(sectionsContainLine.id(), sectionsContainLine.name(), toSections(result));
    }

    private Line findSectionContainsLine(final List<Line> lines, final Deque<Section> sections) {
        return lines.stream()
                .filter(it -> it.contains(sections.peekFirst()))
                .findAny()
                .orElseThrow(() -> new LineException("최단경로 구하는 중 문제 발생"));
    }

    private List<Section> addSectionsToLine(final Line sectionsContainLine, final Deque<Section> sections) {
        final List<Section> result = new ArrayList<>();
        while (isLineContainsSection(sectionsContainLine, sections)) {
            result.add(sections.pollFirst());
        }
        return result;
    }

    private boolean isLineContainsSection(final Line sectionsContainLine, final Deque<Section> sections) {
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
