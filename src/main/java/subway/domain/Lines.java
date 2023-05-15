package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Lines insert(final Line line) {
        validateDuplication(line);
        lines.add(line);
        return new Lines(lines);
    }

    private void validateDuplication(final Line line) {
        if (hasSameName(line)) {
            throw new IllegalArgumentException("이미 존재하는 호선입니다.");
        }
    }

    private boolean hasSameName(final Line line) {
        return lines.stream()
                .anyMatch(eachLine -> eachLine.hasSameName(line));
    }

    public Lines insertStation(final Line line, final Station from, final Station to, final int distance) {
        return modifyLines(line, line.insert(from, to, distance));
    }

    private Lines modifyLines(final Line line, final Line modifiedLine) {
        validateExistence(line);
        lines.remove(line);
        lines.add(modifiedLine);
        return new Lines(lines);
    }

    private void validateExistence(final Line line) {
        if (!lines.contains(line)) {
            throw new IllegalArgumentException("존재하지 않는 호선입니다.");
        }
    }

    public Lines deleteStation(final Line line, final Station station) {
        return modifyLines(line, line.delete(station));
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }
}
