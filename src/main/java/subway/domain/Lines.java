package subway.domain;

import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public Lines(final Line... lines) {
        this(List.of(lines));
    }

    public List<Line> getLines() {
        return lines;
    }
}
