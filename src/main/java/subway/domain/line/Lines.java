package subway.domain.line;

import java.util.List;
import java.util.stream.Collectors;
import subway.exception.ErrorCode;
import subway.exception.GlobalException;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public void validateDuplication(final Line line) {
        final List<String> lineNames = getLineNames();
        if (lineNames.contains(line.getName())) {
            throw new GlobalException(ErrorCode.LINE_NAME_DUPLICATED);
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
