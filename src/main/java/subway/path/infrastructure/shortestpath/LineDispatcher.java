package subway.path.infrastructure.shortestpath;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Sections;
import subway.line.exception.line.LineException;
import subway.path.domain.Path;
import subway.path.exception.PathException;

@Component
public class LineDispatcher {

    public Path dispatch(final Path path, final List<Section> shortestSections) {
        final List<Line> result = new ArrayList<>();
        final Deque<Section> deque = new ArrayDeque<>(shortestSections);
        while (!deque.isEmpty()) {
            result.add(sectionOwner(path, deque));
        }
        return new Path(result);
    }

    private Line sectionOwner(final Path path, final Deque<Section> sections) {
        final Line sectionOwner = findSectionOwner(path, sections);
        final List<Section> result = addSectionsToLine(sectionOwner, sections);
        return new Line(sectionOwner.id(), sectionOwner.name(), sectionOwner.surcharge(), toSections(result));
    }

    private Line findSectionOwner(final Path path, final Deque<Section> sections) {
        return path.lines()
                .stream()
                .filter(it -> it.contains(sections.peekFirst()))
                .findAny()
                .orElseThrow(() -> new PathException("최단경로 구하는 중 문제 발생"));
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
