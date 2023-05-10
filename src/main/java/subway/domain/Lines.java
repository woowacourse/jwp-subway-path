package subway.domain;

import java.util.List;
import subway.exception.GlobalException;

public class Lines {
    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        validate(lines);
        this.lines = lines;
    }

    private void validate(final List<Line> lines) {
        if (lines.isEmpty()) {
            throw new GlobalException("노선이 비어있을 수 없습니다.");
        }

        long distinctSize = lines.stream()
                .distinct()
                .map(Line::getLineInfo)
                .count();

        if (distinctSize != lines.size()) {
            throw new GlobalException("노선 이름이 중복될 수 없습니다.");
        }
    }
}
