package subway.domain;

import subway.exception.DuplicateLineNameException;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines = new ArrayList<>();

    public Line addNewLine(String lineName, Sections sections) {
        validateDuplicate(lineName);
        Line line = new Line(lineName, sections);
        lines.add(line);
        return line;
    }

    private void validateDuplicate(String lineName) {
        if (lines.stream().anyMatch(it -> it.getName().equals(lineName))) {
            throw new DuplicateLineNameException();
        }
    }

    public List<Line> getLines() {
        return lines;
    }
}
