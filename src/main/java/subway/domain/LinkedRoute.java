package subway.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import subway.exception.line.LineException;

public class LinkedRoute {

    private final List<Line> lines;

    private LinkedRoute(final List<Line> lines) {
        this.lines = lines;
    }

    public static LinkedRoute of(final Station start, final List<Line> lines) {
        if (lines.isEmpty()) {
            return new LinkedRoute(new ArrayList<>());
        }
        validateStartStation(start, lines.get(0));
        return new LinkedRoute(linkLines(start, new ArrayList<>(lines)));
    }

    private static void validateStartStation(final Station start, final Line firstLine) {
        if (!firstLine.upTerminal().equals(start) && !firstLine.downTerminal().equals(start)) {
            throw new LineException("경로가 주어진 역으로 시작할 수 없습니다.");
        }
    }

    private static List<Line> linkLines(Station start, final List<Line> lines) {
        final Deque<Line> sorted = new ArrayDeque<>();
        final Line startLine = lines.remove(0);
        sorted.addLast(lineWithStartStation(start, startLine));
        for (final Line line : lines) {
            sorted.addLast(lineWithStartStation(sorted.peekLast().downTerminal(), line));
        }
        return new ArrayList<>(sorted);
    }

    private static Line lineWithStartStation(final Station start, Line line) {
        validateLinked(line, start);
        if (line.downTerminal().equals(start)) {
            return line.reverse();
        }
        return line;
    }

    private static void validateLinked(final Line line, final Station start) {
        if (!line.downTerminal().equals(start) && !line.upTerminal().equals(start)) {
            throw new LineException("노선들이 연결될 수 없습니다");
        }
    }

    public int totalDistance() {
        return lines.stream()
                .flatMap(it -> it.sections().stream())
                .mapToInt(Section::distance)
                .sum();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public List<Line> lines() {
        return new ArrayList<>(lines);
    }
}
