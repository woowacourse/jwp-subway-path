package subway.path.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.exception.line.LineException;

public class Path {

    private final List<Line> lines = new ArrayList<>();

    public Path(final Line... lines) {
        this(Arrays.asList(lines));
    }

    public Path(final List<Line> lines) {
        this.lines.addAll(lines);
    }

    public int totalDistance() {
        return lines.stream()
                .flatMap(it -> it.sections().stream())
                .mapToInt(Section::distance)
                .sum();
    }

    public Path continuousPathWithStartStation(final Station start) {
        validateStartStation(start);
        return linkLines(start);
    }

    private void validateStartStation(final Station start) {
        if (!isStartStationIsUpTerminal(start) && !isStartStationIsDownTerminal(start)) {
            throw new LineException("경로가 주어진 역으로 시작할 수 없습니다.");
        }
    }

    private boolean isStartStationIsUpTerminal(final Station start) {
        return firstLine().upTerminalIsEqualTo(start);
    }

    private Line firstLine() {
        return lines.get(0);
    }

    private boolean isStartStationIsDownTerminal(final Station start) {
        return firstLine().downTerminalIsEqualTo(start);
    }

    private Path linkLines(final Station start) {
        final Deque<Line> sorted = new ArrayDeque<>();
        final ArrayList<Line> lines = new ArrayList<>(this.lines);
        final Line startLine = lines.remove(0);
        sorted.addLast(lineWithStartStation(startLine, start));
        for (final Line line : lines) {
            sorted.addLast(lineWithStartStation(line, sorted.peekLast().downTerminal()));
        }
        return new Path(new ArrayList<>(sorted));
    }

    private Line lineWithStartStation(final Line line, final Station start) {
        validateLinked(line, start);
        if (line.downTerminalIsEqualTo(start)) {
            return line.reverse();
        }
        return line;
    }

    private void validateLinked(final Line line, final Station start) {
        if (!line.downTerminalIsEqualTo(start) && !line.upTerminalIsEqualTo(start)) {
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
        if (!(o instanceof Path)) {
            return false;
        }
        final Path path = (Path) o;
        return Objects.equals(lines, path.lines);
    }

    @Override
    public int hashCode() {
        return lines.hashCode();
    }

    public List<Line> lines() {
        return new ArrayList<>(lines);
    }
}
