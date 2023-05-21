package subway.domain;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Lines findLinesByContainSection(final Sections sections) {
        return lines.stream()
                .filter(line -> sections.stream()
                        .anyMatch(line::containSection))
                .collect(collectingAndThen(toList(), Lines::new));
    }

    public List<Line> getLines() {
        return lines;
    }
}
