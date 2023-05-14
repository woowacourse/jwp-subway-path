package subway.domain.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    // TODO : NotFound 예외 생성
    public Optional<Line> findByLineName(String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst();
    }
}
