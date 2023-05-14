package subway.domain.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        validateDuplicateLineName(lines);
        this.lines = new ArrayList<>(lines);
    }

    private void validateDuplicateLineName(List<Line> lines) {
        int originalLinesSize = lines.size();
        int distinctLinesSize = lines.stream()
                .map(Line::getName)
                .distinct()
                .collect(Collectors.toUnmodifiableList())
                .size();
        if (originalLinesSize != distinctLinesSize) {
            throw new IllegalArgumentException("노선 이름은 중복될 수 없습니다.");
        }
    }

    public Optional<Line> findByLineName(String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst();
    }
}
