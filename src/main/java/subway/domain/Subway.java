package subway.domain;

import java.util.ArrayList;
import java.util.List;
import subway.exception.InvalidLineNameException;
import subway.exception.InvalidSectionException;

public class Subway {

    private final List<Line> lines;

    public Subway(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void add(
            final String lineName,
            final Station base,
            final Station additional,
            final Distance distance,
            final Direction direction
    ) {
        if (lines.stream().anyMatch(line -> line.containsAll(base, additional))) {
            throw new InvalidSectionException("지하철 전체 노선에 이미 존재하는 구간입니다.");
        }

        final Line findLine = lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(InvalidLineNameException::new);

        findLine.add(base, additional, distance, direction);
    }

    public List<Line> getLines() {
        return lines;
    }
}
