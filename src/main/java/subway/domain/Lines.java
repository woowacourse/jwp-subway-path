package subway.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import subway.exception.line.LineException;

public class Lines {

    private final List<Line> lines = new ArrayList<>();

    public Lines(final Line... lines) {
        this(Arrays.asList(lines));
    }

    public Lines(final List<Line> lines) {
        this.lines.addAll(lines);
    }

    public int totalDistance() {
        return lines.stream()
                .flatMap(it -> it.sections().stream())
                .mapToInt(Section::distance)
                .sum();
    }

    public Lines continuousLinesWithStartStation(final Station start) {
        validateStartStation(start);
        return linkLines(start);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    private void validateStartStation(final Station start) {
        if (!firstLine().upTerminal().equals(start) && !firstLine().downTerminal().equals(start)) {
            throw new LineException("경로가 주어진 역으로 시작할 수 없습니다.");
        }
    }

    private Line firstLine() {
        return lines.get(0);
    }

    private Lines linkLines(final Station start) {
        final Deque<Line> sorted = new ArrayDeque<>();
        final Line startLine = lines.remove(0);
        sorted.addLast(lineWithStartStation(start, startLine));
        for (final Line line : lines) {
            sorted.addLast(lineWithStartStation(sorted.peekLast().downTerminal(), line));
        }
        return new Lines(new ArrayList<>(sorted));
    }

    private Line lineWithStartStation(final Station start, Line line) {
        validateLinked(line, start);
        if (line.downTerminal().equals(start)) {
            return line.reverse();
        }
        return line;
    }

    private void validateLinked(final Line line, final Station start) {
        if (!line.downTerminal().equals(start) && !line.upTerminal().equals(start)) {
            throw new LineException("노선들이 연결될 수 없습니다");
        }
    }

    public int size() {
        return lines.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lines)) {
            return false;
        }
        final Lines lines1 = (Lines) o;
        return Objects.equals(lines, lines1.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }

    public List<Line> lines() {
        return new ArrayList<>(lines);
    }
}
