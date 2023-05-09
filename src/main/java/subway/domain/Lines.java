package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public void add(final Line line) {
        validateDuplication(line);
        lines.add(line);
    }

    private void validateDuplication(final Line line) {
        final List<String> lineNames = getLineNames();
        if (lineNames.contains(line.getName())) {
            throw new IllegalArgumentException("노선 이름은 중복될 수 없습니다.");
        }
    }

    private List<String> getLineNames() {
        return lines.stream()
            .map(Line::getName)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Line> getLines() {
        return lines;
    }
}
